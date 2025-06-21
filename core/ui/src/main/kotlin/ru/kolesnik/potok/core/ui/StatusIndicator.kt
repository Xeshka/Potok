package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kolesnik.potok.core.designsystem.AppTheme

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
                if (complete) Color.Green.copy(alpha = 1f)
                else Color.LightGray
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

@Preview(name = "Complete", showBackground = true)
@Composable
fun StatusIndicatorPreview_Complete() {
    AppTheme {
        StatusIndicator(complete = true)
    }
}

@Preview(name = "Incomplete", showBackground = true)
@Composable
fun StatusIndicatorPreview_Incomplete() {
    AppTheme {
        StatusIndicator(complete = false)
    }
}
