package ru.kolesnik.potok.feature.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.model.TaskPayload
import ru.kolesnik.potok.core.ui.AppHeader
import ru.kolesnik.potok.core.ui.SaveButton
import ru.kolesnik.potok.core.ui.TaskHeaderSection
import ru.kolesnik.potok.core.ui.assignees.getFIO
import ru.kolesnik.potok.core.ui.task.InputFieldsSection
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@Preview
@Composable
fun TaskDetailPreview() {
    AppTheme {
        TaskDetailScreen({})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    val titleState = remember(state.task) { mutableStateOf(state.task?.title ?: "") }
    val descriptionState =
        remember(state.task) { mutableStateOf(state.task?.payload?.description ?: "") }
    val deadlineState = remember(state.task) {
        mutableStateOf(
            state.task?.payload?.deadline?.toInstant()?.toEpochMilli()
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                title = null,
                onBackClick = onBackClick,
                rightContent = {
                    state.task?.let {
                        TaskHeaderSection(
                            taskOwner = state.employees.firstOrNull { state.task?.taskOwner == it.employeeId }
                                ?.getFIO() ?: state.task?.taskOwner ?: "Отсутствует",
                            creationDate = state.task?.creationDate,
                        )
                    }
                },
                modifier = modifier
            )
        },
        floatingActionButton = {
            SaveButton(
                onSave = {
                    state.task?.let { originalTask ->
                        val updatedPayload = originalTask.payload?.copy(
                            description = descriptionState.value,
                            deadline = deadlineState.value?.let {
                                OffsetDateTime.ofInstant(
                                    Instant.ofEpochMilli(it),
                                    ZoneId.systemDefault()
                                )
                            },
                            important = state.task?.payload?.important ?: false
                        ) ?: TaskPayload(
                            description = descriptionState.value,
                            important = false
                        )

                        viewModel.handleEvent(
                            TaskDetailEvent.UpdateTask(
                                originalTask.copy(
                                    title = titleState.value,
                                    payload = updatedPayload,
                                ),
                                onSuccess = onBackClick
                            )
                        )
                    }
                },
                modifier = Modifier.padding(start = 32.dp)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .clickable { focusManager.clearFocus() }
                ) {
                    when {
                        state.error != null -> ErrorMessage(state.error!!)
                        state.isLoading -> LoadingIndicator()
                        state.task == null -> NoDataMessage()
                        else -> {
                            InputFieldsSection(
                                titleState = titleState,
                                descriptionState = descriptionState,
                                deadlineState = deadlineState,
                                onDeadlineClick = { showDatePicker = true },
                                task = state.task,
                                focusManager = focusManager,
                                employees = state.employees
                            )
                        }
                    }
                }

                if (state.isSaving) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Сохранение...")
                        }
                    }
                }

                if (showDatePicker) {
                    val dateState =
                        rememberDatePickerState(initialSelectedDateMillis = deadlineState.value)
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            Button(onClick = {
                                dateState.selectedDateMillis?.let {
                                    deadlineState.value = it
                                }
                                showDatePicker = false
                            }) { Text("OK") }
                        }
                    ) {
                        DatePicker(state = dateState)
                    }
                }
            }
        }
    )
}
