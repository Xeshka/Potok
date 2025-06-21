package ru.kolesnik.potok.core.ui

import androidx.compose.ui.graphics.Color

enum class Style(val sourceName: String, val color: Color) {
    STYLE_1("style1", Color(0xFF89CFF0)),
    STYLE_2("style2", Color.Red),
    STYLE_3("style3", Color.Green),
    STYLE_4("style4", Color.Yellow),
    STYLE_5("style5", Color.Magenta),
    STYLE_6("style6", Color.Transparent),
    STYLE_7("style7", Color.Cyan),
    STYLE_8("style8", Color.LightGray);

    companion object {
        private val sourceNames = entries.associateBy { it.sourceName }

        fun bySourceName(source: String) = sourceNames[source] ?: STYLE_1

        fun colorBySourceName(source: String?) =
            (source?.let { sourceNames[source] } ?: STYLE_1).color
    }
}

