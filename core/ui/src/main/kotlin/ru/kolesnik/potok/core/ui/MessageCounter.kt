package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.outlined.FilePresent
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.theme.AppTheme

@Composable
fun MessageCounter(
    count: Int,
    color: Color,
    counterType: CounterType
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(24.dp)
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 6.dp)
    ) {
        Text(
            text = "$count",
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(end = 4.dp)
        )

        when (counterType) {
            CounterType.FILES -> Icon(
                imageVector = Icons.Outlined.FilePresent,
                contentDescription = "Вложения",
                tint = color,
                modifier = Modifier.size(18.dp)
            )

            CounterType.EMPLOYEES -> Icon(
                imageVector = Icons.Outlined.Group,
                contentDescription = "Участники",
                tint = color,
                modifier = Modifier.size(18.dp)
            )

            CounterType.COMMENTS -> Icon(
                imageVector = Icons.AutoMirrored.Outlined.Message,
                contentDescription = "Комментарии",
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Theme - Employees Counter")
@Composable
fun MessageCounterPreview_LightEmployees() {
    AppTheme(darkTheme = false) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MessageCounter(
                count = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                counterType = CounterType.EMPLOYEES
            )
            MessageCounter(
                count = 5,
                color = getStatusColor(true),
                counterType = CounterType.EMPLOYEES
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme - Employees Counter")
@Composable
fun MessageCounterPreview_DarkEmployees() {
    AppTheme(darkTheme = true) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MessageCounter(
                count = 3,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                counterType = CounterType.EMPLOYEES
            )
            MessageCounter(
                count = 5,
                color = getStatusColor(true),
                counterType = CounterType.EMPLOYEES
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Theme - All Counters")
@Composable
fun MessageCounterPreview_LightAll() {
    AppTheme(darkTheme = false) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MessageCounter(3, MaterialTheme.colorScheme.onSurfaceVariant, CounterType.EMPLOYEES)
            MessageCounter(2, MaterialTheme.colorScheme.primary, CounterType.FILES)
            MessageCounter(15, MaterialTheme.colorScheme.tertiary, CounterType.COMMENTS)
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme - All Counters")
@Composable
fun MessageCounterPreview_DarkAll() {
    AppTheme(darkTheme = true) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MessageCounter(3, MaterialTheme.colorScheme.onSurfaceVariant, CounterType.EMPLOYEES)
            MessageCounter(2, MaterialTheme.colorScheme.primary, CounterType.FILES)
            MessageCounter(15, MaterialTheme.colorScheme.tertiary, CounterType.COMMENTS)
        }
    }
}