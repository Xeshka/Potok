package ru.kolesnik.potok.core.network.di

import androidx.tracing.trace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.cookie.InMemoryCookieStore
import ru.kolesnik.potok.core.network.cookie.JavaNetCookieJar
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.retrofit.RetrofitSyncFullNetworkApi
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.time.Duration
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

class AuthAuthenticator(
    private val apiProvider: dagger.Lazy<RetrofitSyncFullNetworkApi>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        when (response.code) {
            401, 403 -> {
                runBlocking {
                    apiProvider.get().auth()
                }
                return response.request.newBuilder().build()
            }
            502, 503 -> {
                throw Exception("AUTH_SERVER_OFF: ${response.code}")
            }
            else -> throw Exception("ERROR: ${response.code}")
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthenticator(
        apiProvider: dagger.Lazy<RetrofitSyncFullNetworkApi>
    ): Authenticator = AuthAuthenticator(apiProvider)

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true // Игнорировать неизвестные ключи
        isLenient = true         // Разрешить нестрогий JSON
        encodeDefaults = true    // Сериализовать значения по умолчанию
    }

    @Provides
    @Singleton
    fun provideRetrofitSyncFullNetworkApi(
        callFactory: Call.Factory,
        networkJson: Json
    ): RetrofitSyncFullNetworkApi {
        return Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory(callFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RetrofitSyncFullNetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager =
        CookieManager(InMemoryCookieStore("cumulo"), CookiePolicy.ACCEPT_ALL)

    @Provides
    @Singleton
    fun okHttpCallFactory(
        appSSLFactory: AppSSLFactory,
        cookieManager: CookieManager,
        authenticator: Authenticator
    ): Call.Factory = trace("NiaOkHttpClient") {
        val sslFactory = appSSLFactory.getSSLFactory()
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .retryOnConnectionFailure(true)
            .readTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .cookieJar(JavaNetCookieJar(cookieManager))
            .sslSocketFactory(sslFactory.sslSocketFactory, sslFactory.trustManager.get())
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            ).build()
    }

}
