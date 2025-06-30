package ru.kolesnik.potok.core.network.di

import android.util.Log
import androidx.tracing.trace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.cookie.InMemoryCookieStore
import ru.kolesnik.potok.core.network.cookie.JavaNetCookieJar

import ru.kolesnik.potok.core.network.datasource.*
import ru.kolesnik.potok.core.network.datasource.impl.*
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.BACKEND_URL

class AuthAuthenticator(
    private val apiProvider: dagger.Lazy<AuthDataSource>
) : Authenticator {
    private var attemptCount = 0

    override fun authenticate(route: Route?, response: Response): Request? {
        when (response.code) {
            401, 403 -> {
                if (attemptCount >= 3) {
                    Log.e("Auth", "Too many auth attempts")
                    return null
                }
                attemptCount++
                try {
                    runBlocking {
                        apiProvider.get().auth()
                    }
                    return response.request.newBuilder()
                        .removeHeader("Cookie")
                        .build()
                } catch (e: Exception) {
                    Log.e("Auth", "Authentication failed", e)
                    return null
                }
            }

            502, 503 -> {
                throw Exception("AUTH_SERVER_OFF: ${response.code}")
            }

            else -> {
                attemptCount = 0
                throw Exception("ERROR: ${response.code}")
            }
        }
    }
}

class RetrofitAuthDataSource @Inject constructor(
    private val api: AuthApi
) : AuthDataSource {
    override suspend fun auth(): Boolean {
        return try {
            val response = api.auth()
            if (response.isSuccessful) {
                Log.d("Auth", "Authentication successful")

                // Проверяем наличие кук в ответе
                val cookies = response.headers()["Set-Cookie"]
                if (cookies.isNullOrEmpty()) {
                    Log.w("Auth", "No cookies in auth response")
                } else {
                    Log.d("Auth", "Received cookies: $cookies")
                }

                true
            } else {
                Log.e("Auth", "Authentication failed: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("Auth", "Authentication error", e)
            false
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true      // Игнорировать неизвестные ключи
        explicitNulls = false         // Не требовать null для отсутствующих полей
        coerceInputValues = true      // Автоматически преобразовывать неверные значения
        prettyPrint = true            // Для красивого вывода при сериализации
        isLenient = true              // Ленивый парсинг
        encodeDefaults = true         // Сериализовать значения по умолчанию
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager =
        CookieManager(InMemoryCookieStore("app_cookies"), CookiePolicy.ACCEPT_ALL)

    @Provides
    @Singleton
    fun provideRetrofit(
        callFactory: Call.Factory,
        networkJson: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory(callFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(
        appSSLFactory: AppSSLFactory,
        cookieManager: CookieManager,
        authenticator: Authenticator
    ): Call.Factory {
        val sslFactory = appSSLFactory.getSSLFactory()
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .retryOnConnectionFailure(true)
            .readTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .cookieJar(JavaNetCookieJar(cookieManager))
            .sslSocketFactory(sslFactory.sslSocketFactory, sslFactory.trustManager.get())
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthDataSource(api: AuthApi): AuthDataSource =
        RetrofitAuthDataSource(api)

    @Provides
    @Singleton
    fun provideAuthenticator(
        authDataSource: dagger.Lazy<AuthDataSource>
    ): Authenticator = AuthAuthenticator(authDataSource)


    // Логгирующий интерцептор
    private class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            logRequest(request)

            val response = try {
                chain.proceed(request)
            } catch (e: Exception) {
                logFailure(request, e)
                throw e
            }

            return logResponse(response)
        }

        private fun logRequest(request: Request) {
            Log.d("Network", "--> ${request.method} ${request.url}")
            Log.d("Network", "Headers: ${request.headers}")
            Log.d("Network", "Cookies: ${request.header("Cookie")}")
        }

        private fun logResponse(response: Response): Response {
            val duration = response.receivedResponseAtMillis - response.sentRequestAtMillis
            Log.d("Network", "<-- ${response.code} ${response.message} (${duration}ms)")
            Log.d("Network", "Headers: ${response.headers}")
            return response
        }

        private fun logFailure(request: Request, e: Exception) {
            Log.e("Network", "${request.method} ${request.url} failed: ${e.message}")
        }
    }
}