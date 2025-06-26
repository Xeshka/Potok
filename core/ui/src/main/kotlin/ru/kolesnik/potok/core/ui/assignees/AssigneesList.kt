package ru.kolesnik.potok.core.ui.assignees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.theme.AppTheme
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.model.TaskAssignee

@Composable
fun AssigneesList(
    assignees: List<TaskAssignee>,
    employees: List<Employee>
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            text = "Ответственные:",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (assignees.isEmpty()) {
            Text(
                text = "Не назначены",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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

@Preview(showBackground = true, name = "Light Theme - Combined")
@Composable
fun AssigneesListPreview_LightCombined() {
    AppTheme(darkTheme = false) {
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

@Preview(showBackground = true, name = "Dark Theme - Combined")
@Composable
fun AssigneesListPreview_DarkCombined() {
    AppTheme(darkTheme = true) {
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