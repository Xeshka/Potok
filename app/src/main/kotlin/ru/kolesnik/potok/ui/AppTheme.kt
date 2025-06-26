package ru.kolesnik.potok.ui

import androidx.compose.runtime.Composable
import ru.kolesnik.potok.core.designsystem.AppTheme

/**
 * Обертка для темы приложения
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    AppTheme(
        darkTheme = darkTheme,
        disableDynamicTheming = true,
        content = content
    )
}