package ru.kolesnik.potok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.kolesnik.potok.core.network.ssl.AppSSLFactory
import ru.kolesnik.potok.core.network.ssl.MtlsSSLFactoryState
import ru.kolesnik.potok.navigation.AppNavHost
import ru.kolesnik.potok.ui.AppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSSLFactory: AppSSLFactory

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        appSSLFactory.init(this)
        enableEdgeToEdge()

        setContent {
            val sslState = appSSLFactory.state.collectAsStateWithLifecycle()
            val windowSizeClass = calculateWindowSizeClass(this)
            splashScreen.setKeepOnScreenCondition {
                sslState.value is MtlsSSLFactoryState.Loading
            }

            if (sslState.value is MtlsSSLFactoryState.Done) {
                CompositionLocalProvider() {
                    AppTheme {
                        AppContent(
                            windowSizeClass = windowSizeClass,
                            onShowSnackbar = { message, action ->
                                // Простая реализация snackbar
                                false
                            }
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun AppContent(
    windowSizeClass: WindowSizeClass,
    onShowSnackbar: suspend (message: String, action: String?) -> Boolean
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            AppNavHost(
                modifier = Modifier.padding(innerPadding),
                windowSizeClass = windowSizeClass,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    AppTheme {
        // Для превью используем заглушку
    }
}