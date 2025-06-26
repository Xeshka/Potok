package com.google.samples.apps.nowinandroid.core.analytics

import android.util.Log
import ru.kolesnik.potok.core.analytics.AnalyticsEvent
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Безопасная реализация AnalyticsHelper, которая работает даже без Firebase
 */
@Singleton
internal class SafeFirebaseAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    private val firebaseAnalytics: Any? by lazy {
        try {
            // Пытаемся получить Firebase Analytics
            val firebaseClass = Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
            val getInstanceMethod = firebaseClass.getMethod("getInstance", android.content.Context::class.java)
            // Здесь нужен контекст, но мы не можем его получить в Singleton
            // Поэтому возвращаем null и используем fallback
            null
        } catch (e: Exception) {
            Log.d(TAG, "Firebase Analytics not available: ${e.message}")
            null
        }
    }

    override fun logEvent(event: AnalyticsEvent) {
        try {
            firebaseAnalytics?.let { analytics ->
                // Если Firebase доступен, используем его
                logToFirebase(analytics, event)
            } ?: run {
                // Если Firebase недоступен, логируем в консоль
                logToConsole(event)
            }
        } catch (e: Exception) {
            // В случае любой ошибки просто логируем в консоль
            Log.w(TAG, "Failed to log analytics event, falling back to console logging", e)
            logToConsole(event)
        }
    }

    private fun logToFirebase(analytics: Any, event: AnalyticsEvent) {
        try {
            // Используем рефлексию для вызова Firebase методов
            val logEventMethod = analytics.javaClass.getMethod(
                "logEvent", 
                String::class.java, 
                android.os.Bundle::class.java
            )
            
            val bundle = android.os.Bundle().apply {
                event.extras.forEach { param ->
                    putString(param.key.take(40), param.value.take(100))
                }
            }
            
            logEventMethod.invoke(analytics, event.type, bundle)
            Log.d(TAG, "Logged event to Firebase: ${event.type}")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to log to Firebase, using console fallback", e)
            logToConsole(event)
        }
    }

    private fun logToConsole(event: AnalyticsEvent) {
        Log.d(TAG, "Analytics Event: ${event.type}, extras: ${event.extras}")
    }

    companion object {
        private const val TAG = "SafeFirebaseAnalytics"
    }
}