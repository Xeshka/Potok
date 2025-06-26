package ru.kolesnik.potok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import ru.kolesnik.potok.core.analytics.LocalAnalyticsHelper
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.ui.AppState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Настройка splash screen
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is MainActivityUiState.Loading
        }

        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val darkTheme = shouldUseDarkTheme(uiState)

            AppTheme(
                darkTheme = darkTheme,
                disableDynamicTheming = shouldDisableDynamicTheming(uiState)
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    LocalAnalyticsHelper provides analyticsHelper
                ) {
                    AppState()
                }
            }
        }
    }
}

/**
 * Определяет, следует ли использовать темную тему
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> uiState.userData.useDarkTheme
        ThemeBrand.ANDROID -> isSystemInDarkTheme()
    }
}

/**
 * Определяет, следует ли отключить динамические цвета
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> uiState.userData.useDynamicColor.not()
        ThemeBrand.ANDROID -> false
    }
}