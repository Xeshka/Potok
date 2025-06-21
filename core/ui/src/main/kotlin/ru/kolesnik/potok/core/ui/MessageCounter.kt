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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme

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

@Preview(showBackground = true, name = "Employees Counter")
@Composable
fun MessageCounterPreview_Employees() {
    AppTheme {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MessageCounter(
            count = 3,
            color = Color.Gray,
            counterType = CounterType.EMPLOYEES
        )
        MessageCounter(
            count = 5,
            color = Color(0xFF4CAF50),
            counterType = CounterType.EMPLOYEES
        )
    }
        }
}

@Preview(showBackground = true, name = "Files Counter")
@Composable
fun MessageCounterPreview_Files() {
    AppTheme {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MessageCounter(
                count = 2,
                color = Color.Blue,
                counterType = CounterType.FILES
            )
            MessageCounter(
                count = 0,
                color = Color.Red,
                counterType = CounterType.FILES
            )
        }
    }
}

@Preview(showBackground = true, name = "Comments Counter")
@Composable
fun MessageCounterPreview_Comments() {
    AppTheme {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MessageCounter(
                count = 15,
                color = Color(0xFF9C27B0),
                counterType = CounterType.COMMENTS
            )
            MessageCounter(
                count = 99,
                color = Color(0xFFFF9800),
                counterType = CounterType.COMMENTS
            )
        }
    }
}

@Preview(showBackground = true, name = "All Counters")
@Composable
fun MessageCounterPreview_All() {
    AppTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MessageCounter(3, Color.Gray, CounterType.EMPLOYEES)
            MessageCounter(2, Color.Blue, CounterType.FILES)
            MessageCounter(15, Color(0xFF9C27B0), CounterType.COMMENTS)
        }
    }
}