package ru.kolesnik.potok.feature.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.OffsetDateTime

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

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Синхронизация данных...")
    }
}

@Composable
fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = error, color = Color.Red)
    }
}

@Composable
fun NoDataMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Нет данных для отображения")
    }
}

fun <T : Any> T.toMap(): Map<String, Any?> {
    return this::class.java.declaredFields
        .associate { field ->
            field.isAccessible = true
            val value = field.get(this)
            val processedValue = when (value) {
                is OffsetDateTime -> value.toString()
                else -> value
            }
            field.name to processedValue
        }
}
