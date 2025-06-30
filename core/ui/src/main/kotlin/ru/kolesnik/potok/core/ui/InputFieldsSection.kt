package ru.kolesnik.potok.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.IsuDate
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskPayload
import java.util.UUID

@Composable
fun InputFieldsSection(
    titleState: MutableState<String>,
    descriptionState: MutableState<String>,
    deadlineState: MutableState<Long?>,
    onDeadlineClick: () -> Unit,
    task: Task?,
    focusManager: FocusManager
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        TaskTitleField(
            value = titleState.value,
            onValueChange = { titleState.value = it },
            focusRequester = focusRequester,
            focusManager = focusManager
        )

        TaskDescriptionField(
            value = descriptionState.value,
            onValueChange = { descriptionState.value = it }
        )

        DeadlineField(
            deadlineMillis = deadlineState.value,
            onDeadlineClick = onDeadlineClick
        )

        task?.payload?.let { payload ->
            ImportantSwitch(
                initialChecked = payload.important ?: false,
                onCheckedChange = { payload.important = it }
            )
        }

        AssigneesList(assignees = task?.assignees ?: emptyList())
    }
}

@Preview(showBackground = true, name = "New Task Form")
@Composable
fun InputFieldsSectionPreview_NewTask() {
    val titleState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val deadlineState = remember { mutableStateOf<Long?>(null) }
    val focusManager = LocalFocusManager.current

    AppTheme {
        InputFieldsSection(
            titleState = titleState,
            descriptionState = descriptionState,
            deadlineState = deadlineState,
            onDeadlineClick = { /* handle click */ },
            task = null,
            focusManager = focusManager
        )
    }
}

@Preview(showBackground = true, name = "Edit Existing Task")
@Composable
fun InputFieldsSectionPreview_EditTask() {
    val mockTask = Task(
        title = "Важная задача",

        creationDate = IsuDate.now(),
        assignees = listOf(
            TaskAssignee("user_1", complete = true),
            TaskAssignee("user_2", complete = false)
        ),
        id = UUID.randomUUID().toString(),
        taskOwner = UUID.randomUUID().toString(),
        payload = TaskPayload(
            title = "Важная задача",
            important = true,
            description = "Необходимо выполнить срочное задание",
        )
    )

    val titleState = remember { mutableStateOf(mockTask.title) }
    val descriptionState = remember { mutableStateOf(mockTask.payload?.description ?: "") }
    val deadlineState = remember { mutableStateOf<Long?>(System.currentTimeMillis() + 86_400_000) }
    val focusManager = LocalFocusManager.current

    AppTheme {
        InputFieldsSection(
            titleState = titleState,
            descriptionState = descriptionState,
            deadlineState = deadlineState,
            onDeadlineClick = { /* handle click */ },
            task = mockTask,
            focusManager = focusManager
        )
    }
}