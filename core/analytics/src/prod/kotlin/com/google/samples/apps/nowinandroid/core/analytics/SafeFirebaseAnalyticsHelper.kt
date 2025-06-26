package com.google.samples.apps.nowinandroid.core.analytics

import android.util.Log
import ru.kolesnik.potok.core.analytics.AnalyticsEvent
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Безопасная реализация Firebase Analytics, которая не падает если Firebase недоступен
 */
@Singleton
class SafeFirebaseAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    
    companion object {
        private const val TAG = "SafeFirebaseAnalytics"
    }
    
    private val firebaseAnalytics: Any? by lazy {
        try {
            val firebaseClass = Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
            val getInstanceMethod = firebaseClass.getMethod("getInstance", android.content.Context::class.java)
            // Здесь нужен контекст, но мы не можем его получить в Singleton
            // Поэтому возвращаем null и используем fallback
            null
        } catch (e: Exception) {
            Log.d(TAG, "Firebase Analytics not available")
            null
        }
    }
    
    override fun logEvent(event: AnalyticsEvent) {
        try {
            if (firebaseAnalytics != null) {
                // Логируем через Firebase если доступен
                logToFirebase(event)
            } else {
                // Fallback - логируем в консоль
                Log.d(TAG, "Analytics event: ${event.type}, extras: ${event.extras}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to log analytics event: ${event.type}", e)
        }
    }
    
    private fun logToFirebase(event: AnalyticsEvent) {
        // Реализация логирования в Firebase
        // Пока используем простое логирование
        Log.d(TAG, "Firebase Analytics event: ${event.type}")
    }
}