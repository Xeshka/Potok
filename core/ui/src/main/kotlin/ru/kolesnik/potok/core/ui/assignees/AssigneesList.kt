package ru.kolesnik.potok.core.ui.assignees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.model.TaskAssignee


@Preview(showBackground = true)
@Composable
fun AssigneesListPreview_Combined() {
    AppTheme {
        Column {
            AssigneesList(assignees = emptyList(), employees = emptyList())
            Spacer(modifier = Modifier.height(24.dp))
            AssigneesList(
                assignees = listOf(
                    TaskAssignee("user_1", true),
                    TaskAssignee("user_2", false)
                ),
                employees = emptyList()
            )
        }
    }
}

@Composable
fun AssigneesList(
    assignees: List<TaskAssignee>,
    employees: List<Employee>
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = "Ответственные:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (assignees.isEmpty()) {
            Text(
                text = "Не назначены",
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            Column {
                assignees.forEach { assignee ->
                    AssigneeItem(
                        assignee = assignee,
                        employees = employees
                    )
                }
            }
        }
    }
}