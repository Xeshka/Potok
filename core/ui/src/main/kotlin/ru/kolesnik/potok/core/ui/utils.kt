package ru.kolesnik.potok.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.kolesnik.potok.core.designsystem.theme.ComponentColors
import ru.kolesnik.potok.core.designsystem.theme.LifeAreaColors

enum class Style(val sourceName: String, val color: Color) {
    STYLE_1("style1", LifeAreaColors.Style1),
    STYLE_2("style2", LifeAreaColors.Style2),
    STYLE_3("style3", LifeAreaColors.Style3),
    STYLE_4("style4", LifeAreaColors.Style4),
    STYLE_5("style5", LifeAreaColors.Style5),
    STYLE_6("style6", LifeAreaColors.Style6),
    STYLE_7("style7", LifeAreaColors.Style7),
    STYLE_8("style8", LifeAreaColors.Style8);

    companion object {
        private val sourceNames = entries.associateBy { it.sourceName }

        fun bySourceName(source: String) = sourceNames[source] ?: STYLE_1

        fun colorBySourceName(source: String?) = LifeAreaColors.getColorByStyle(source)
    }
}

/**
 * Получить цвет фона карточки задачи в зависимости от темы
 */
@Composable
fun getTaskCardBackgroundColor(): Color {
    return MaterialTheme.colorScheme.surfaceContainer
}

/**
 * Получить цвет фона поля ввода в зависимости от темы
 */
@Composable
fun getInputFieldBackgroundColor(): Color {
    return MaterialTheme.colorScheme.surfaceContainerHigh
}

/**
 * Получить цвет разделителя в зависимости от темы
 */
@Composable
fun getDividerColor(): Color {
    return MaterialTheme.colorScheme.outlineVariant
}

/**
 * Получить цвет для статуса выполнения
 */
@Composable
fun getStatusColor(isCompleted: Boolean): Color {
    return if (isCompleted) {
        ComponentColors.SuccessGreen
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
}

/**
 * Получить цвет для просроченных задач
 */
@Composable
fun getOverdueColor(): Color {
    return ComponentColors.ErrorRed
}

/**
 * Получить цвет для важных задач
 */
@Composable
fun getImportantColor(): Color {
    return ComponentColors.WarningOrange
}