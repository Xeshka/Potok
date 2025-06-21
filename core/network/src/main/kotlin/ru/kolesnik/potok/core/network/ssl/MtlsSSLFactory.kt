package ru.kolesnik.potok.core.network.ssl

import android.app.Activity
import android.content.Context
import android.security.KeyChain
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.altindag.ssl.SSLFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.R
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MtlsSSLFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
) : AppSSLFactory {
    override var state = MutableStateFlow<MtlsSSLFactoryState>(MtlsSSLFactoryState.Loading)
        private set

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val sslFactoryBuilder: SSLFactory.Builder = DEFAULT_BUILDER

    private fun chooseCertificate(activity: Activity, callback: () -> Unit = {}) {
        KeyChain.choosePrivateKeyAlias(
            activity,
            { alias ->
                alias?.let {
                    saveSelectedAlias(alias)
                    updateFactoryWithCert(it)
                } ?: state.update { MtlsSSLFactoryState.Done }
                callback()
            },
            null,       //
            null,         // Issuers
            BuildConfig.BACKEND_URL,           // Host
            -1,             // Port
            null            // Alias to select
        )
    }

    private fun updateFactoryWithCert(alias: String) {
        val clientCert = KeyChain.getCertificateChain(context, alias)!!
        val clientKey = KeyChain.getPrivateKey(context, alias)!!
        Log.d("SSL", clientCert[0].subjectX500Principal.name)
        sslFactoryBuilder.withIdentityMaterial(clientKey, NO_PASSWORD, *clientCert)
        state.update { MtlsSSLFactoryState.Done }
    }

    private fun saveSelectedAlias(alias: String) {
        prefs.edit {
            putString(ALIAS, alias)
        }
    }

    override fun init(activity: Activity) {
        val alias = prefs.getString(ALIAS, null)
        if (alias == null) {
            chooseCertificate(activity)
        } else {
            CoroutineScope(coroutineDispatcher).launch {
                updateFactoryWithCert(alias)
            }
        }
    }

    override fun select(activity: Activity, callback: () -> Unit) {
        state.value = MtlsSSLFactoryState.Loading
        chooseCertificate(activity, callback)
    }

    override fun reset() {
        prefs.edit {
            this.remove(ALIAS)
        }
    }

    override fun getSSLFactory(): SSLFactory {
        val sberCa = context.resources.openRawResource(R.raw.core_network_sber_ca)
        return sslFactoryBuilder
            .withTrustMaterial(sberCa, "changeit".toCharArray())
            .build()
    }

    companion object {
        private val DEFAULT_BUILDER = SSLFactory.builder()
            .withDefaultTrustMaterial()
            .withSystemTrustMaterial()
        private val ALIAS = "alias"
        private val NO_PASSWORD: CharArray? = null
    }
}