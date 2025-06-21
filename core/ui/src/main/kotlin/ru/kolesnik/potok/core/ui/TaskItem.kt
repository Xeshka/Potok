package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.FlowId
import ru.kolesnik.potok.core.model.IsuDate
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

object PreviewData {
    fun createMockTask() = TaskMain(
        id = UUID.randomUUID().toString(),
        title = UUID.randomUUID().toString(),
        taskOwner = UUID.randomUUID().toString(),
        creationDate = IsuDate.now(),
        internalId = 12345,
        lifeAreaPlacement = 1,
        flowPlacement = 1,
        assignees = listOf(
            TaskAssignee(
                employeeId = UUID.randomUUID().toString(),
                complete = Random.nextBoolean()
            )
        ),
        commentCount = 3,
        attachmentCount = 2,
        lifeAreaId = UUID.randomUUID(),
        flowId = UUID.randomUUID(),
        deadline = IsuDate.now(),
    )
}

@Composable
fun TaskItem(
    task: TaskMain,
    isClose: Boolean,
    onTaskClick: (String) -> Unit,
    onCloseClick: (String, UUID) -> Unit,
    onDelete: (String) -> Unit,
    closeFlow: FlowId
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onTaskClick(task.id) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.title,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(end = 48.dp),
                    textDecoration = if (isClose) TextDecoration.LineThrough else TextDecoration.None
                )

                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(36.dp)
                        .weight(1f)
                        .align(Alignment.Top)
                        .offset(y = (-4).dp)
                ) {
                    Icon(Icons.Default.MoreVert, "Меню")

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(fraction = 0.5f)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Редактировать") },
                            onClick = {
                                expanded = false
                                onTaskClick(task.id)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Завершить") },
                            onClick = {
                                expanded = false
                                onCloseClick(task.id, closeFlow)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Удалить") },
                            onClick = {
                                expanded = false
                                onDelete(task.id)
                            }
                        )
                    }
                }
            }

            val completedCount = task.assignees?.count { it.complete } ?: 0
            val pendingCount = task.assignees?.count { !it.complete } ?: 0
            val hasAssignees = completedCount > 0 || pendingCount > 0
            val hasDeadline = task.deadline != null
            val showSecondRow = hasDeadline || hasAssignees

            if (showSecondRow) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (hasDeadline) {
                        Text(
                            text = task.deadline
                                ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                ?.let { formattedDate ->
                                    if (task.deadline!! <= OffsetDateTime.now()) {
                                        "❗$formattedDate❗"
                                    } else {
                                        formattedDate
                                    }
                                } ?: "",
                            color = if (task.deadline?.isBefore(OffsetDateTime.now()) == true) Color.Red else Color.Gray,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    if (hasAssignees) {
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf(
                                completedCount to Color(0xFF4CAF50),
                                pendingCount to Color.Gray
                            ).forEachIndexed { index, (count, color) ->
                                if (count > 0) {
                                    if (index > 0) Spacer(Modifier.width(6.dp))
                                    MessageCounter(
                                        count = count,
                                        color = color,
                                        CounterType.EMPLOYEES
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

enum class CounterType {
    FILES, COMMENTS, EMPLOYEES
}

@Preview(showBackground = true, name = "Default Task Item")
@Composable
fun TaskItemPreview_Default() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Обычная задача с нормальным заголовком",
        deadline = OffsetDateTime.now().plusDays(3)
    )

    AppTheme {
        TaskItem(
            task = mockTask,
            isClose = false,
            onTaskClick = {},
            onCloseClick = { _, _ -> },
            onDelete = {},
            closeFlow = UUID.randomUUID()
        )
    }
}

@Preview(showBackground = true, name = "Closed Task")
@Composable
fun TaskItemPreview_Closed() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Завершенная задача",
        deadline = OffsetDateTime.now().minusDays(5)
    )

    AppTheme {
        TaskItem(
            task = mockTask,
            isClose = true,
            onTaskClick = {},
            onCloseClick = { _, _ -> },
            onDelete = {},
            closeFlow = UUID.randomUUID()
        )
    }
}

@Preview(showBackground = true, name = "Overdue Task")
@Composable
fun TaskItemPreview_Overdue() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Просроченная задача с очень длинным названием, которое должно обрезаться",
        deadline = OffsetDateTime.now().minusDays(1)
    )

    AppTheme {
        TaskItem(
            task = mockTask,
            isClose = false,
            onTaskClick = {},
            onCloseClick = { _, _ -> },
            onDelete = {},
            closeFlow = UUID.randomUUID()
        )
    }
}

@Preview(showBackground = true, name = "With Many Assignees")
@Composable
fun TaskItemPreview_ManyAssignees() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Задача с множеством исполнителей",
        assignees = List(5) {
            TaskAssignee(
                employeeId = UUID.randomUUID().toString(),
                complete = it % 2 == 0
            )
        }
    )

    AppTheme {
        TaskItem(
            task = mockTask,
            isClose = false,
            onTaskClick = {},
            onCloseClick = { _, _ -> },
            onDelete = {},
            closeFlow = UUID.randomUUID()
        )
    }
}

@Preview(showBackground = true, name = "With Menu Open")
@Composable
fun TaskItemPreview_MenuOpen() {
    val mockTask = PreviewData.createMockTask()
    var expanded by remember { mutableStateOf(true) }

    AppTheme {
        Box(modifier = Modifier.size(360.dp, 200.dp)) {
            TaskItem(
                task = mockTask,
                isClose = false,
                onTaskClick = {},
                onCloseClick = { _, _ -> },
                onDelete = {},
                closeFlow = UUID.randomUUID(),
            )
        }
    }
}
