package ru.kolesnik.potok.core.designsystem.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Светлая цветовая схема приложения
 */
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary40,
    onPrimary = AppColors.Neutral99,
    primaryContainer = AppColors.Primary90,
    onPrimaryContainer = AppColors.Primary40,
    
    secondary = AppColors.Secondary40,
    onSecondary = AppColors.Neutral99,
    secondaryContainer = AppColors.Secondary90,
    onSecondaryContainer = AppColors.Secondary40,
    
    tertiary = AppColors.Tertiary40,
    onTertiary = AppColors.Neutral99,
    tertiaryContainer = AppColors.Tertiary90,
    onTertiaryContainer = AppColors.Tertiary40,
    
    error = AppColors.Error40,
    onError = AppColors.Neutral99,
    errorContainer = AppColors.Error90,
    onErrorContainer = AppColors.Error40,
    
    background = AppColors.Neutral99,
    onBackground = AppColors.Neutral10,
    
    surface = AppColors.Surface,
    onSurface = AppColors.Neutral10,
    surfaceVariant = AppColors.NeutralVariant90,
    onSurfaceVariant = AppColors.NeutralVariant30,
    
    surfaceTint = AppColors.Primary40,
    inverseSurface = AppColors.InverseSurface,
    inverseOnSurface = AppColors.InverseOnSurface,
    inversePrimary = AppColors.InversePrimary,
    
    outline = AppColors.Outline,
    outlineVariant = AppColors.OutlineVariant,
    scrim = AppColors.Scrim,
    
    surfaceBright = AppColors.SurfaceBright,
    surfaceDim = AppColors.SurfaceDim,
    surfaceContainer = AppColors.SurfaceContainer,
    surfaceContainerHigh = AppColors.SurfaceContainerHigh,
    surfaceContainerHighest = AppColors.SurfaceContainerHighest,
    surfaceContainerLow = AppColors.SurfaceContainerLow,
    surfaceContainerLowest = AppColors.SurfaceContainerLowest,
)

/**
 * Темная цветовая схема приложения
 */
private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Primary80,
    onPrimary = AppColors.Primary40,
    primaryContainer = AppColors.Primary40,
    onPrimaryContainer = AppColors.Primary90,
    
    secondary = AppColors.Secondary80,
    onSecondary = AppColors.Secondary40,
    secondaryContainer = AppColors.Secondary40,
    onSecondaryContainer = AppColors.Secondary90,
    
    tertiary = AppColors.Tertiary80,
    onTertiary = AppColors.Tertiary40,
    tertiaryContainer = AppColors.Tertiary40,
    onTertiaryContainer = AppColors.Tertiary90,
    
    error = AppColors.Error80,
    onError = AppColors.Error40,
    errorContainer = AppColors.Error40,
    onErrorContainer = AppColors.Error90,
    
    background = AppColors.Neutral10,
    onBackground = AppColors.Neutral90,
    
    surface = AppColors.SurfaceDark,
    onSurface = AppColors.Neutral90,
    surfaceVariant = AppColors.NeutralVariant30,
    onSurfaceVariant = AppColors.NeutralVariant80,
    
    surfaceTint = AppColors.Primary80,
    inverseSurface = AppColors.Neutral90,
    inverseOnSurface = AppColors.Neutral20,
    inversePrimary = AppColors.Primary40,
    
    outline = AppColors.OutlineDark,
    outlineVariant = AppColors.OutlineVariantDark,
    scrim = AppColors.Scrim,
    
    surfaceBright = AppColors.SurfaceBrightDark,
    surfaceDim = AppColors.SurfaceDimDark,
    surfaceContainer = AppColors.SurfaceContainerDark,
    surfaceContainerHigh = AppColors.SurfaceContainerHighDark,
    surfaceContainerHighest = AppColors.SurfaceContainerHighestDark,
    surfaceContainerLow = AppColors.SurfaceContainerLowDark,
    surfaceContainerLowest = AppColors.SurfaceContainerLowestDark,
)

/**
 * Основная тема приложения
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val emptyGradientColors = GradientColors(
        container = colorScheme.surfaceColorAtElevation(2.dp)
    )
    
    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )

    val gradientColors = when {
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    val tintTheme = when {
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S