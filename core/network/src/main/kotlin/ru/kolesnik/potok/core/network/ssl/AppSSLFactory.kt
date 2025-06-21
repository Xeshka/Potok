package ru.kolesnik.potok.core.network.ssl

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow
import nl.altindag.ssl.SSLFactory

interface AppSSLFactory {
    val state: StateFlow<MtlsSSLFactoryState>
    fun init(activity: Activity)
    fun select(activity: Activity, callback: () -> Unit)
    fun reset()
    fun getSSLFactory(): SSLFactory
}

sealed interface MtlsSSLFactoryState {
    data object Loading : MtlsSSLFactoryState
    data object Done : MtlsSSLFactoryState
}
