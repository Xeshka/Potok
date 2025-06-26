package ru.kolesnik.potok.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import ru.kolesnik.potok.core.designsystem.theme.AppTheme as DesignSystemTheme

/**
 * Основная тема приложения
 * Обертка над темой из design system
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit
) {
    DesignSystemTheme(
        darkTheme = darkTheme,
        disableDynamicTheming = disableDynamicTheming,
        content = content
    )
}