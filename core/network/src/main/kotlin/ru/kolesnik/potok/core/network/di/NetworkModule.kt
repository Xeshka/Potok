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
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.cookie.InMemoryCookieStore
import ru.kolesnik.potok.core.network.cookie.JavaNetCookieJar
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.time.Duration
import javax.inject.Qualifier
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiRetrofit

class AuthAuthenticator(
    private val apiProvider: dagger.Lazy<AuthApi>
) : Authenticator {
    private var attemptCount = 0
    private var isAuthenticating = false

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("AuthAuthenticator", "Authentication needed for: ${response.request.url}")
        
        when (response.code) {
            401, 403 -> {
                if (attemptCount >= 3) {
                    Log.e("AuthAuthenticator", "Too many auth attempts")
                    return null
                }
                
                if (isAuthenticating) {
                    Log.d("AuthAuthenticator", "Already authenticating, skipping")
                    return null
                }
                
                attemptCount++
                isAuthenticating = true
                
                try {
                    Log.d("AuthAuthenticator", "Attempting authentication...")
                    runBlocking {
                        apiProvider.get().auth()
                    }
                    Log.d("AuthAuthenticator", "Authentication successful")
                    
                    return response.request.newBuilder()
                        .removeHeader("Cookie")
                        .build()
                } catch (e: Exception) {
                    Log.e("AuthAuthenticator", "Authentication failed", e)
                    return null
                } finally {
                    isAuthenticating = false
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
    fun provideAuthenticator(
        @AuthRetrofit apiProvider: dagger.Lazy<AuthApi>
    ): Authenticator = AuthAuthenticator(apiProvider)

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
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
    ): Call.Factory = trace("CumuloOkHttpClient") {
        val sslFactory = appSSLFactory.getSSLFactory()
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .retryOnConnectionFailure(true)
            .readTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))
            .writeTimeout(Duration.ofMinutes(1))
            .cookieJar(JavaNetCookieJar(cookieManager))
            .sslSocketFactory(sslFactory.sslSocketFactory, sslFactory.trustManager.get())
            .addInterceptor(AuthInterceptor(cookieManager))
            .addInterceptor(PerformanceInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        callFactory: Call.Factory,
        networkJson: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory(callFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @ApiRetrofit
    fun provideApiRetrofit(
        callFactory: Call.Factory,
        networkJson: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory(callFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        @AuthRetrofit retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    // API провайдеры
    @Provides
    @Singleton
    fun provideChecklistApi(@ApiRetrofit retrofit: Retrofit): ChecklistApi {
        return retrofit.create(ChecklistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLifeAreaApi(@ApiRetrofit retrofit: Retrofit): LifeAreaApi {
        return retrofit.create(LifeAreaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLifeFlowApi(@ApiRetrofit retrofit: Retrofit): LifeFlowApi {
        return retrofit.create(LifeFlowApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBoardApi(@ApiRetrofit retrofit: Retrofit): BoardApi {
        return retrofit.create(BoardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskApi(@ApiRetrofit retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchApi(@ApiRetrofit retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApi(@ApiRetrofit retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
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
                code >= 500 -> ANSI_RED
                code >= 400 -> ANSI_YELLOW
                code >= 300 -> ANSI_BLUE
                else -> ANSI_GREEN
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
                val source = body.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val content = buffer.clone().readUtf8()

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
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body?.writeTo(buffer)
                val content = buffer.readUtf8()

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

        private val ANSI_RESET = "\u001B[0m"
        private val ANSI_RED = "\u001B[31m"
        private val ANSI_GREEN = "\u001B[32m"
        private val ANSI_YELLOW = "\u001B[33m"
        private val ANSI_BLUE = "\u001B[34m"
    }
}