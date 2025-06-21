package ru.kolesnik.potok.core.ui.assignees

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
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.ui.StatusIndicator

@Composable
fun AssigneeItem(
    assignee: TaskAssignee,
    employees: List<Employee>
) {
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
            text = employees.firstOrNull { it.employeeId == assignee.employeeId }?.getFIO()
                ?: assignee.employeeId,
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 14.sp
        )

        StatusIndicator(
            complete = assignee.complete,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

fun Employee.getFIO() =
    "$lastName ${firstName.first()}.${middleName?.let { "${it.first()}." } ?: ""}"

@Preview(showBackground = true)
@Composable
fun AssigneeListPreview() {
    AppTheme {
        Column {
            AssigneeItem(
                assignee = TaskAssignee(
                    employeeId = "user_1",
                    complete = true
                ),
                employees = emptyList()
            )
            AssigneeItem(
                assignee = TaskAssignee(
                    employeeId = "user_2",
                    complete = false
                ),
                employees = emptyList()
            )
        }
    }
}