package ru.kolesnik.potok.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import ru.kolesnik.potok.core.designsystem.theme.AppTheme as DesignSystemTheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    DesignSystemTheme(
        darkTheme = darkTheme,
        disableDynamicTheming = !dynamicColor,
        content = content
    )
}