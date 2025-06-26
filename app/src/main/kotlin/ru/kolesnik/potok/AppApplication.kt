package ru.kolesnik.potok

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {
    
    companion object {
        private const val TAG = "AppApplication"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        Log.d(TAG, "Application starting...")
        
        // ✅ Инициализация Firebase только если доступен
        initializeFirebaseIfAvailable()
        
        // ✅ Инициализация других компонентов
        initializeOtherComponents()
        
        Log.d(TAG, "Application initialized successfully")
    }
    
    private fun initializeFirebaseIfAvailable() {
        try {
            // Проверяем, доступен ли Firebase
            val firebaseAppClass = Class.forName("com.google.firebase.FirebaseApp")
            val initializeAppMethod = firebaseAppClass.getMethod("initializeApp", android.content.Context::class.java)
            initializeAppMethod.invoke(null, this)
            Log.d(TAG, "Firebase initialized successfully")
        } catch (e: ClassNotFoundException) {
            // Firebase не подключен - это нормально для demo-версии
            Log.d(TAG, "Firebase not available - running in demo mode")
        } catch (e: Exception) {
            // Ошибка инициализации Firebase - логируем, но не падаем
            Log.w(TAG, "Firebase initialization failed", e)
        }
    }
    
    private fun initializeOtherComponents() {
        try {
            // Здесь можно добавить инициализацию других компонентов
            // Например: Crashlytics, Analytics, etc.
            Log.d(TAG, "Other components initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize other components", e)
        }
    }
}