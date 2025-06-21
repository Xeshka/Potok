package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.TaskAssignee

@Composable
fun AssigneeItem(assignee: TaskAssignee) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = assignee.employeeId,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 14.sp
        )

        StatusIndicator(
            complete = assignee.complete,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AssigneeListPreview() {
    AppTheme {
        Column {
            AssigneeItem(
                assignee = TaskAssignee(
                    employeeId = "user_1",
                    complete = true
                )
            )
            AssigneeItem(
                assignee = TaskAssignee(
                    employeeId = "user_2",
                    complete = false
                )
            )
        }
    }
}