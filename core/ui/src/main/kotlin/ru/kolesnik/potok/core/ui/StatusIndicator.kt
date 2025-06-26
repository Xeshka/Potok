package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kolesnik.potok.core.designsystem.theme.AppTheme

@Composable
fun StatusIndicator(
    complete: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(
                if (complete) {
                    getStatusColor(true)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (complete) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Выполнено",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(name = "Light Theme - Complete", showBackground = true)
@Composable
fun StatusIndicatorPreview_LightComplete() {
    AppTheme(darkTheme = false) {
        StatusIndicator(complete = true)
    }
}

@Preview(name = "Light Theme - Incomplete", showBackground = true)
@Composable
fun StatusIndicatorPreview_LightIncomplete() {
    AppTheme(darkTheme = false) {
        StatusIndicator(complete = false)
    }
}

@Preview(name = "Dark Theme - Complete", showBackground = true)
@Composable
fun StatusIndicatorPreview_DarkComplete() {
    AppTheme(darkTheme = true) {
        StatusIndicator(complete = true)
    }
}

@Preview(name = "Dark Theme - Incomplete", showBackground = true)
@Composable
fun StatusIndicatorPreview_DarkIncomplete() {
    AppTheme(darkTheme = true) {
        StatusIndicator(complete = false)
    }
}