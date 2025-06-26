package ru.kolesnik.potok.core.network.di

import android.util.Log
import androidx.tracing.trace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.AuthApi
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.cookie.InMemoryCookieStore
import ru.kolesnik.potok.core.network.cookie.JavaNetCookieJar
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

class AuthAuthenticator(
    private val apiProvider: dagger.Lazy<AuthApi>,
    private val cookieManager: CookieManager
) : Authenticator {
    private val isAuthenticating = AtomicBoolean(false)
    
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("AuthAuthenticator", "Authentication needed for: ${response.request.url}")
        
        // Проверяем, что это действительно 401 ошибка
        if (response.code != 401) {
            Log.d("AuthAuthenticator", "Not a 401 error, code: ${response.code}")
            return null
        }
        
        // Предотвращаем множественные одновременные попытки авторизации
        if (isAuthenticating.getAndSet(true)) {
            Log.d("AuthAuthenticator", "Authentication already in progress")
            return null
        }
        
        return try {
            Log.d("AuthAuthenticator", "Starting authentication...")
            
            // Очищаем старые куки
            cookieManager.cookieStore.removeAll()
            Log.d("AuthAuthenticator", "Cleared old cookies")
            
            // Выполняем авторизационный запрос
            runBlocking {
                apiProvider.get().auth()
            }
            Log.d("AuthAuthenticator", "Authentication successful")
            
            // Возвращаем оригинальный запрос для повтора
            response.request.newBuilder().build()
            
        } catch (e: Exception) {
            Log.e("AuthAuthenticator", "Authentication failed", e)
            null
        } finally {
            isAuthenticating.set(false)
        }
    }
}

class AuthInterceptor(
    private val cookieManager: CookieManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Логируем куки для отладки
        val cookies = cookieManager.cookieStore.get(request.url.toUri())
        Log.d("AuthInterceptor", "Request to ${request.url} with ${cookies.size} cookies")
        
        return chain.proceed(request)
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager =
        CookieManager(InMemoryCookieStore("potok"), CookiePolicy.ACCEPT_ALL)

    @Provides
    @Singleton
    fun provideAuthenticator(
        apiProvider: dagger.Lazy<AuthApi>,
        cookieManager: CookieManager
    ): Authenticator = AuthAuthenticator(apiProvider, cookieManager)

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
    ): AuthApi {
        return Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory(callFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AuthApi::class.java)
    }

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
            .cookieJar(JavaNetCookieJar(cookieManager))
            .addInterceptor(AuthInterceptor(cookieManager))
            .retryOnConnectionFailure(true)
            .readTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .sslSocketFactory(sslFactory.sslSocketFactory, sslFactory.trustManager.get())
            .addInterceptor(PerformanceInterceptor())
            .build()
    }

    private class PerformanceInterceptor : Interceptor {
        companion object {
            var LOG_HEADERS = false
            var LOG_BODY_META = false
            var LOG_BODY_CONTENT = false
            const val MAX_BODY_LOG_LENGTH = 2048
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val startTime = System.nanoTime()

            logBasicRequestInfo(request)
            if (LOG_HEADERS) logRequestDetails(request)

            try {
                val response = chain.proceed(request)
                val elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0

                logBasicResponseInfo(request, response, elapsedTime)
                if (LOG_HEADERS) logResponseDetails(response)

                return response
            } catch (e: Exception) {
                val elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0
                Log.e("Network", "${request.method} ${request.url} failed in ${"%.2f".format(elapsedTime)}ms", e)
                throw e
            }
        }

        private fun logBasicRequestInfo(request: Request) {
            Log.d("Network", "--> ${request.method} ${request.url}")
        }

        private fun logBasicResponseInfo(request: Request, response: Response, elapsedMs: Double) {
            val code = response.code
            val message = response.message
            val color = when {
                code >= 500 -> ANSI_RED    // Server errors
                code >= 400 -> ANSI_YELLOW  // Client errors
                code >= 300 -> ANSI_BLUE    // Redirects
                else -> ANSI_GREEN          // Success
            }

            Log.d("Network", "<-- $color$code $message${ANSI_RESET} ${request.url} (${"%.2f".format(elapsedMs)}ms)")
        }

        private fun logResponseDetails(response: Response) {
            try {
                val headers = response.headers
                    .joinToString("\n    ") { "${it.first}: ${redactSensitive(it.first, it.second)}" }
                Log.d("Network", "Response Headers:\n    $headers")

                if (LOG_BODY_META) {
                    response.body?.let { body ->
                        val contentType = body.contentType()?.toString() ?: "unknown"
                        val size = body.contentLength().takeIf { it != -1L }?.let { "$it bytes" } ?: "unknown size"
                        Log.d("Network", "Response Body: $contentType ($size)")

                        if (LOG_BODY_CONTENT && isTextContentType(body.contentType())) {
                            // Исправление: правильный способ получить тело ответа
                            val content = getResponseBodyContent(body)
                            Log.d("Network", "Response Body Content:\n${content.prettyPrintIfJson()}")
                        }
                    } ?: Log.d("Network", "Response Body: none")
                }
            } catch (e: Exception) {
                Log.e("Network", "Failed to log response details", e)
            }
        }

        private fun getResponseBodyContent(body: ResponseBody): String {
            return try {
                // Создаем копию тела для чтения
                val source = body.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val content = buffer.clone().readUtf8()

                // Ограничиваем длину
                if (content.length > MAX_BODY_LOG_LENGTH) {
                    content.substring(0, MAX_BODY_LOG_LENGTH) + "... [truncated]"
                } else {
                    content
                }
            } catch (e: Exception) {
                "Failed to read response body: ${e.message}"
            }
        }

        private fun getRequestBodyContent(request: Request): String {
            return try {
                // Копируем тело запроса
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body?.writeTo(buffer)
                val content = buffer.readUtf8()

                // Ограничиваем длину
                if (content.length > MAX_BODY_LOG_LENGTH) {
                    content.substring(0, MAX_BODY_LOG_LENGTH) + "... [truncated]"
                } else {
                    content
                }
            } catch (e: Exception) {
                "Failed to read request body: ${e.message}"
            }
        }

        private fun logRequestDetails(request: Request) {
            try {
                val headers = request.headers
                    .joinToString("\n    ") { "${it.first}: ${redactSensitive(it.first, it.second)}" }
                Log.d("Network", "Request Headers:\n    $headers")

                if (LOG_BODY_META) {
                    request.body?.let { body ->
                        val contentType = body.contentType()?.toString() ?: "unknown"
                        val size = body.contentLength().takeIf { it != -1L }?.let { "$it bytes" } ?: "unknown size"
                        Log.d("Network", "Request Body: $contentType ($size)")

                        if (LOG_BODY_CONTENT && isTextContentType(body.contentType())) {
                            val content = getRequestBodyContent(request)
                            Log.d("Network", "Request Body Content:\n${content.prettyPrintIfJson()}")
                        }
                    } ?: Log.d("Network", "Request Body: none")
                }
            } catch (e: Exception) {
                Log.e("Network", "Failed to log request details", e)
            }
        }

        private fun redactSensitive(headerName: String, value: String): String {
            return when {
                headerName.equals("Authorization", ignoreCase = true) ->
                    if (value.startsWith("Bearer ")) "Bearer [REDACTED]" else "[REDACTED]"

                headerName.equals("Cookie", ignoreCase = true) ->
                    value.split(";")
                        .joinToString("; ") {
                            if (it.contains("=")) {
                                val parts = it.split("=", limit = 2)
                                "${parts[0]}=[${parts[1].take(2)}...]"
                            } else {
                                it
                            }
                        }

                else -> value
            }
        }

        private fun isTextContentType(contentType: MediaType?): Boolean {
            if (contentType == null) return false
            val type = contentType.type
            val subtype = contentType.subtype
            return type == "text" ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html") ||
                    subtype.contains("form")
        }

        private fun peekRequestBody(request: Request): String? {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body?.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: Exception) {
                null
            }
        }

        private fun String.prettyPrintIfJson(): String {
            return if (contains(Regex("^\\s*[{\\[]"))) {
                try {
                    val json = JSONObject(this)
                    json.toString(2)
                } catch (e: JSONException) {
                    try {
                        val json = JSONArray(this)
                        json.toString(2)
                    } catch (e: JSONException) {
                        this
                    }
                }
            } else {
                this
            }
        }

        // ANSI цвета для терминала
        private val ANSI_RESET = "\u001B[0m"
        private val ANSI_RED = "\u001B[31m"
        private val ANSI_GREEN = "\u001B[32m"
        private val ANSI_YELLOW = "\u001B[33m"
        private val ANSI_BLUE = "\u001B[34m"
    }

}