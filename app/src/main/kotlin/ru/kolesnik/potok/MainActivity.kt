package ru.kolesnik.potok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import ru.kolesnik.potok.core.analytics.LocalAnalyticsHelper
import ru.kolesnik.potok.core.designsystem.theme.AppTheme
import ru.kolesnik.potok.core.ui.LoadingIndicator
import ru.kolesnik.potok.navigation.AppNavHost
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState())

        // Обновляем UI состояние
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState = it
                }
            }
        }

        // Держим splash screen пока приложение инициализируется
        splashScreen.setKeepOnScreenCondition {
            uiState.isLoading
        }

        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)

            // Обновляем системные бары
            DisposableEffect(darkTheme) {
                enableEdgeToEdge()
                onDispose {}
            }

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
            ) {
                AppTheme(
                    darkTheme = darkTheme,
                    disableDynamicTheming = !uiState.shouldUseDynamicColor(),
                ) {
                    MainActivityContent(
                        uiState = uiState,
                        onRetry = viewModel::retry,
                        onClearError = viewModel::clearError
                    )
                }
            }
        }
    }
}

@Composable
private fun MainActivityContent(
    uiState: MainActivityUiState,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Показываем ошибки через Snackbar
    uiState.error?.let { error ->
        androidx.compose.runtime.LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Повторить"
            ).let { result ->
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    onRetry()
                }
                onClearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                uiState.isInitialized -> {
                    AppNavHost()
                }
                else -> {
                    // Показываем экран ошибки или повторной инициализации
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Здесь можно добавить UI для ошибки инициализации
                    }
                }
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState.userData.darkThemeConfig) {
    DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    DarkThemeConfig.LIGHT -> false
    DarkThemeConfig.DARK -> true
}

private fun MainActivityUiState.shouldUseDynamicColor(): Boolean {
    return userData.useDynamicColor
}

private val MainActivityUiState.userData: UserData
    get() = UserData() // Заглушка, в реальном приложении здесь будут настройки пользователя