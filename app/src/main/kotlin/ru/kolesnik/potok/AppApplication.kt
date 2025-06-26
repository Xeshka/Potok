package ru.kolesnik.potok

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // ✅ Инициализация Firebase только если доступен
        initializeFirebaseIfAvailable()
    }
    
    private fun initializeFirebaseIfAvailable() {
        try {
            // Проверяем, доступен ли Firebase
            val firebaseAppClass = Class.forName("com.google.firebase.FirebaseApp")
            val initializeAppMethod = firebaseAppClass.getMethod("initializeApp", android.content.Context::class.java)
            initializeAppMethod.invoke(null, this)
        } catch (e: ClassNotFoundException) {
            // Firebase не подключен - это нормально для demo-версии
            android.util.Log.d("AppApplication", "Firebase not available - running in demo mode")
        } catch (e: Exception) {
            // Ошибка инициализации Firebase - логируем, но не падаем
            android.util.Log.w("AppApplication", "Firebase initialization failed", e)
        }
    }
}