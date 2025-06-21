package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kolesnik.potok.core.designsystem.AppTheme
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val inputFieldColor = Color(0xFFF8F9FA)

@Preview(showBackground = true)
@Composable
fun DeadlineFieldInteractivePreview() {
    var deadline by remember { mutableStateOf<Long?>(null) }

    AppTheme {
        Column {
            DeadlineField(
                deadlineMillis = deadline,
                onDeadlineClick = {
                    deadline = System.currentTimeMillis()
                }
            )
        }
    }
}

@Composable
fun DeadlineField(
    deadlineMillis: Long?,
    onDeadlineClick: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val deadlineText = deadlineMillis?.let {
        OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(it),
            ZoneId.systemDefault()
        ).format(formatter)
    } ?: "Укажите срок выполнения"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable { onDeadlineClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = deadlineText,
            onValueChange = {},
            modifier = Modifier.weight(1f),
            enabled = false,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputFieldColor,
                unfocusedContainerColor = inputFieldColor,
                disabledContainerColor = inputFieldColor,
                errorContainerColor = inputFieldColor,
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Календарь",
                    tint = Color.Gray
                )
            }
        )
    }
}
