package ru.kolesnik.potok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.metrics.performance.JankStats
import dagger.hilt.android.AndroidEntryPoint
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import ru.kolesnik.potok.core.network.ssl.MtlsSSLFactoryState
import ru.kolesnik.potok.ui.AppFun
import ru.kolesnik.potok.ui.rememberAppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSSLFactory: AppSSLFactory

    /**
     * Lazily inject [JankStats], which is used to track jank throughout the app.
     */
    //@Inject
    //lateinit var lazyStats: dagger.Lazy<JankStats>

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)



        appSSLFactory.init(this)
        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        //splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val sslState = appSSLFactory.state.collectAsStateWithLifecycle()

            if (sslState.value is MtlsSSLFactoryState.Done) {
                CompositionLocalProvider() {
                    AppTheme {
                        AppFun(
                            appState = rememberAppState()
                        )
                    }
                }
            }
        }
    }

    //override fun onResume() {
    //    super.onResume()
    //    lazyStats.get().isTrackingEnabled = true
    //}
//
    //override fun onPause() {
    //    super.onPause()
    //    lazyStats.get().isTrackingEnabled = false
    //}
}