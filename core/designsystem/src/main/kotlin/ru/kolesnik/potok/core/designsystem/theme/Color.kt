package ru.kolesnik.potok.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// Основные цвета приложения
object AppColors {
    // Primary colors
    val Primary40 = Color(0xFF006D36)
    val Primary80 = Color(0xFF0EE37C)
    val Primary90 = Color(0xFF5AFF9D)
    
    // Secondary colors
    val Secondary40 = Color(0xFF4F6352)
    val Secondary80 = Color(0xFFB7CCB8)
    val Secondary90 = Color(0xFFD3E8D3)
    
    // Tertiary colors
    val Tertiary40 = Color(0xFF3A656F)
    val Tertiary80 = Color(0xFFA2CED9)
    val Tertiary90 = Color(0xFFBEEAF6)
    
    // Error colors
    val Error40 = Color(0xFFBA1A1A)
    val Error80 = Color(0xFFFFB4AB)
    val Error90 = Color(0xFFFFDAD6)
    
    // Neutral colors
    val Neutral10 = Color(0xFF1A1C1A)
    val Neutral20 = Color(0xFF2F312E)
    val Neutral90 = Color(0xFFE2E3DE)
    val Neutral95 = Color(0xFFF0F1EC)
    val Neutral99 = Color(0xFFFBFDF7)
    
    // Neutral variant colors
    val NeutralVariant30 = Color(0xFF414941)
    val NeutralVariant50 = Color(0xFF727971)
    val NeutralVariant60 = Color(0xFF8B938A)
    val NeutralVariant80 = Color(0xFFC1C9BF)
    val NeutralVariant90 = Color(0xFFDDE5DB)
    
    // Surface colors
    val Surface = Color(0xFFFBFDF7)
    val SurfaceDim = Color(0xFFDBDDD7)
    val SurfaceBright = Color(0xFFFBFDF7)
    val SurfaceContainerLowest = Color(0xFFFFFFFF)
    val SurfaceContainerLow = Color(0xFFF5F7F1)
    val SurfaceContainer = Color(0xFFEFEFEA)
    val SurfaceContainerHigh = Color(0xFFE9EBE5)
    val SurfaceContainerHighest = Color(0xFFE3E5DF)
    
    // Dark surface colors
    val SurfaceDark = Color(0xFF111311)
    val SurfaceDimDark = Color(0xFF111311)
    val SurfaceBrightDark = Color(0xFF373937)
    val SurfaceContainerLowestDark = Color(0xFF0C0E0C)
    val SurfaceContainerLowDark = Color(0xFF191C19)
    val SurfaceContainerDark = Color(0xFF1D201D)
    val SurfaceContainerHighDark = Color(0xFF272B27)
    val SurfaceContainerHighestDark = Color(0xFF323532)
    
    // Outline colors
    val Outline = Color(0xFF727971)
    val OutlineVariant = Color(0xFFC1C9BF)
    val OutlineDark = Color(0xFF8B938A)
    val OutlineVariantDark = Color(0xFF414941)
    
    // Inverse colors
    val InverseSurface = Color(0xFF2F322E)
    val InverseOnSurface = Color(0xFFE2E3DE)
    val InversePrimary = Color(0xFF0EE37C)
    
    // Scrim
    val Scrim = Color(0xFF000000)
}

// Дополнительные цвета для UI компонентов
object ComponentColors {
    val TaskCardBackground = Color(0xFFF8F9FA)
    val TaskCardBackgroundDark = Color(0xFF1E1E1E)
    
    val InputFieldBackground = Color(0xFFF8F9FA)
    val InputFieldBackgroundDark = Color(0xFF2A2A2A)
    
    val DividerLight = Color(0xFFE0E0E0)
    val DividerDark = Color(0xFF3A3A3A)
    
    val SuccessGreen = Color(0xFF4CAF50)
    val WarningOrange = Color(0xFFFF9800)
    val ErrorRed = Color(0xFFF44336)
    val InfoBlue = Color(0xFF2196F3)
}

// Цвета для статусов задач
object StatusColors {
    val CompletedGreen = Color(0xFF4CAF50)
    val PendingGray = Color(0xFF9E9E9E)
    val OverdueRed = Color(0xFFF44336)
    val ImportantOrange = Color(0xFFFF9800)
}

// Цвета для стилей областей жизни
object LifeAreaColors {
    val Style1 = Color(0xFF89CFF0) // Голубой
    val Style2 = Color(0xFFFF6B6B) // Красный
    val Style3 = Color(0xFF4ECDC4) // Бирюзовый
    val Style4 = Color(0xFFFFE66D) // Желтый
    val Style5 = Color(0xFFFF8B94) // Розовый
    val Style6 = Color(0xFFA8E6CF) // Зеленый
    val Style7 = Color(0xFFB4A7D6) // Фиолетовый
    val Style8 = Color(0xFFD4A574) // Оранжевый
    
    fun getColorByStyle(styleName: String?): Color {
        return when (styleName) {
            "style1" -> Style1
            "style2" -> Style2
            "style3" -> Style3
            "style4" -> Style4
            "style5" -> Style5
            "style6" -> Style6
            "style7" -> Style7
            "style8" -> Style8
            else -> Style1
        }
    }
}