/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.analytics

import android.util.Log
import ru.kolesnik.potok.core.analytics.AnalyticsEvent
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import javax.inject.Inject

/**
 * Безопасная реализация AnalyticsHelper для продакшн-версии.
 * Обрабатывает случаи, когда Firebase может быть недоступен.
 */
internal class SafeFirebaseAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    companion object {
        private const val TAG = "SafeFirebaseAnalyticsHelper"
    }

    private val firebaseAnalyticsHelper: AnalyticsHelper? by lazy {
        try {
            // Пытаемся создать Firebase Analytics Helper
            val firebaseClass = Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
            val getInstance = firebaseClass.getMethod("getInstance", android.content.Context::class.java)
            
            // Если Firebase доступен, создаем настоящий helper
            FirebaseAnalyticsHelper(
                firebaseAnalytics = getInstance.invoke(null, null) as com.google.firebase.analytics.FirebaseAnalytics
            )
        } catch (e: ClassNotFoundException) {
            Log.d(TAG, "Firebase Analytics not available - using stub implementation")
            null
        } catch (e: Exception) {
            Log.w(TAG, "Failed to initialize Firebase Analytics", e)
            null
        }
    }

    override fun logEvent(event: AnalyticsEvent) {
        try {
            firebaseAnalyticsHelper?.logEvent(event) ?: run {
                // Fallback к логированию в консоль
                Log.d(TAG, "Analytics event: ${event.type}, extras: ${event.extras}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to log analytics event", e)
        }
    }
}