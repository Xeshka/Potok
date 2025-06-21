package ru.kolesnik.potok.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import ru.kolesnik.potok.core.designsystem.component.AppBackground
import ru.kolesnik.potok.navigation.AppNavHost

@Composable
fun SystemBarColorHandler() {
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    val activity = view.context as? Activity ?: return
    val window = activity.window
    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !isDarkTheme
            isAppearanceLightNavigationBars = !isDarkTheme
        }
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
    }
}

@Composable
fun AppFun(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    SystemBarColorHandler()

    AppBackground(modifier = modifier) {
        AppInt(
            appState = appState,
        )
    }
}

@Composable
internal fun AppInt(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Vertical,
                    ),
                ),
        ) {
            Box(
                modifier = Modifier.consumeWindowInsets(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
            ) {
                AppNavHost(
                    appState = appState,
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun AppFunPreview() {
    MaterialTheme {
        val context = LocalContext.current
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()

        val appState = remember {
            AppState(
                navController = navController,
                coroutineScope = coroutineScope
            )
        }

        CompositionLocalProvider(LocalContext provides context) {
            AppFun(
                appState = appState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}