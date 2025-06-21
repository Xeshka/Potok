package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.FlowStatus
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.model.TaskPayload
import java.time.OffsetDateTime
import java.util.UUID

@Composable
fun LifeAreaItem(
    onTaskDelete: (String) -> Unit,
    onTaskClick: (String) -> Unit,
    onCloseClick: (String, UUID) -> Unit,
    onCreateClick: (Task) -> Unit,
    lifeArea: LifeArea,
    taskMains: List<TaskMain>?,
    flows: List<LifeFlow>
) {

    var showNewTaskDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var showClosedTasks by remember { mutableStateOf(false) }
    val closed = UUID.fromString(flows.last().id)

    val (activeTasks, closedTasks) = taskMains
        ?.sortedBy { it.lifeAreaPlacement }
        ?.partition { it.flowId != closed }
        ?.let { Pair(it.first, it.second) }
        ?: Pair(emptyList(), emptyList())

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        Style.colorBySourceName(lifeArea.style)
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val title = lifeArea.title.trim()
                Text(
                    text = if (title.length > 25)title.substring(0, 30) + "..." else title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { /* TODO: Обработать клик по заголовку Првалимся во флоу*/ }
                        .padding(end = 8.dp)
                )
                Text(
                    text = "${taskMains?.size ?: 0}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                )
                IconButton(
                    onClick = { /* TODO: Обработать клик по меню Создание/Изменение/Удаление Досок */ },
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    activeTasks.forEach { task ->
                        TaskItem(
                            task = task,
                            isClose = false, // Все активные задачи не завершены
                            onTaskClick = onTaskClick,
                            onCloseClick = onCloseClick,
                            onDelete = onTaskDelete,
                            closeFlow = closed
                        )
                    }
                    if (closedTasks.isNotEmpty()) {
                        OutlinedButton(
                            onClick = { showClosedTasks = !showClosedTasks },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Завершенные (${closedTasks.size})")
                                Icon(
                                    imageVector = if (showClosedTasks) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                                    contentDescription = "Показать/скрыть"
                                )
                            }
                        }

                        if (showClosedTasks) {
                            closedTasks.forEach { task ->
                                TaskItem(
                                    task = task,
                                    isClose = true, // Все задачи в этом списке завершены
                                    onTaskClick = onTaskClick,
                                    onCloseClick = onCloseClick,
                                    onDelete = onTaskDelete,
                                    closeFlow = closed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                }
                OutlinedButton(
                    onClick = { showNewTaskDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Black
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Style.colorBySourceName(lifeArea.style),
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Новая задача ")
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Добавить",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (showNewTaskDialog) {
                    AlertDialog(
                        onDismissRequest = { showNewTaskDialog = false },
                        title = { Text("Создание новой задачи") },
                        text = {
                            OutlinedTextField(
                                value = newTaskTitle,
                                onValueChange = { newTaskTitle = it },
                                label = { Text("Название задачи") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val newTask = Task(
                                        title = newTaskTitle,
                                        lifeAreaId = lifeArea.id,
                                        flowId = UUID.fromString(flows.first().id),
                                        taskOwner = "449927", //TODO тут нужно научиться получать данные о текущем пользователе
                                        payload = TaskPayload(
                                            title = newTaskTitle,
                                            description = "",
                                            important = false,
                                            assignees = emptyList()
                                        )
                                    )
                                    onCreateClick(newTask)
                                    showNewTaskDialog = false
                                }
                            ) {
                                Text("Создать")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    showNewTaskDialog = false
                                    newTaskTitle = ""
                                }
                            ) {
                                Text("Отмена")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Default Life Area")
@Composable
fun LifeAreaItemPreview_Default() {
    val mockLifeArea = LifeArea(
        id = UUID.randomUUID(),
        title = "Работа",
        style = Style.STYLE_6.name,
        placement = 1,
        tagsId = 1,
        isDefault = false,
        isTheme = false,
        shared = null
    )

    val mockFlows = listOf(
        LifeFlow(
            id = UUID.randomUUID().toString(),
            status = FlowStatus.NEW,
            areaId = mockLifeArea.id.toString(),
            title = "NEW",
            style = Style.STYLE_1.name,
            placement = kotlin.random.Random(20).nextInt()
        ),
        LifeFlow(
            id = UUID.randomUUID().toString(),
            status = FlowStatus.COMPLETED,
            areaId = mockLifeArea.id.toString(),
            title = "NEW",
            style = Style.STYLE_1.name,
            placement = kotlin.random.Random(20).nextInt()
        )
    )

    val mockTasks = listOf(
        TaskMain(
            id = UUID.randomUUID().toString(),
            title = "Важная задача",
            flowId = UUID.fromString(mockFlows[0].id),
            taskOwner = "Человек",
            deadline = OffsetDateTime.now(),
            creationDate = OffsetDateTime.now().minusDays(2)
        ),
        TaskMain(
            id = UUID.randomUUID().toString(),
            title = "Завершенная задача",
            taskOwner = "Человек",
            deadline = OffsetDateTime.now(),
            flowId = UUID.fromString(mockFlows[1].id),
            creationDate = OffsetDateTime.now().minusDays(5)
        )
    )

    AppTheme {
        LifeAreaItem(
            onTaskDelete = {},
            onTaskClick = {},
            onCloseClick = { _, _ -> },
            onCreateClick = {},
            lifeArea = mockLifeArea,
            taskMains = mockTasks,
            flows = mockFlows
        )
    }
}
