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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        colors = CardDefaults.cardColors(
            containerColor = getTaskCardBackgroundColor()
        )
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
                    textDecoration = if (isClose) TextDecoration.LineThrough else TextDecoration.None,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(36.dp)
                        .weight(1f)
                        .align(Alignment.Top)
                        .offset(y = (-4).dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert, 
                        "Меню",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(fraction = 0.5f)
                    ) {
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Редактировать",
                                    color = MaterialTheme.colorScheme.onSurface
                                ) 
                            },
                            onClick = {
                                expanded = false
                                onTaskClick(task.id)
                            }
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Завершить",
                                    color = MaterialTheme.colorScheme.onSurface
                                ) 
                            },
                            onClick = {
                                expanded = false
                                onCloseClick(task.id, closeFlow)
                            }
                        )
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    "Удалить",
                                    color = getOverdueColor()
                                ) 
                            },
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (hasDeadline) {
                        val isOverdue = task.deadline?.isBefore(OffsetDateTime.now()) == true
                        Text(
                            text = task.deadline
                                ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                ?.let { formattedDate ->
                                    if (isOverdue) {
                                        "❗$formattedDate❗"
                                    } else {
                                        formattedDate
                                    }
                                } ?: "",
                            color = if (isOverdue) getOverdueColor() else MaterialTheme.colorScheme.onSurfaceVariant,
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
                                completedCount to getStatusColor(true),
                                pendingCount to getStatusColor(false)
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

@Preview(showBackground = true, name = "Light Theme - Default Task")
@Composable
fun TaskItemPreview_Light() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Обычная задача с нормальным заголовком",
        deadline = OffsetDateTime.now().plusDays(3)
    )

    AppTheme(darkTheme = false) {
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

@Preview(showBackground = true, name = "Dark Theme - Default Task")
@Composable
fun TaskItemPreview_Dark() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Обычная задача с нормальным заголовком",
        deadline = OffsetDateTime.now().plusDays(3)
    )

    AppTheme(darkTheme = true) {
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

@Preview(showBackground = true, name = "Light Theme - Overdue Task")
@Composable
fun TaskItemPreview_LightOverdue() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Просроченная задача с очень длинным названием, которое должно обрезаться",
        deadline = OffsetDateTime.now().minusDays(1)
    )

    AppTheme(darkTheme = false) {
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

@Preview(showBackground = true, name = "Dark Theme - Overdue Task")
@Composable
fun TaskItemPreview_DarkOverdue() {
    val mockTask = PreviewData.createMockTask().copy(
        title = "Просроченная задача с очень длинным названием, которое должно обрезаться",
        deadline = OffsetDateTime.now().minusDays(1)
    )

    AppTheme(darkTheme = true) {
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