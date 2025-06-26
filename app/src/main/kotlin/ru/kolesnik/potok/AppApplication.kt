package ru.kolesnik.potok

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.datasource.repository.SyncRepository
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application() {
    @Inject
    lateinit var syncRepository: SyncRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Инициализируем синхронизацию данных при запуске приложения
        applicationScope.launch {
            try {
                syncRepository.sync()
            } catch (e: Exception) {
                // Обработка ошибок синхронизации
                e.printStackTrace()
            }
        }
    }

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().penaltyDeath().build(),
            )
        }
    }
}