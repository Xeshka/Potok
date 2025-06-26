package ru.kolesnik.potok.feature.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.network.model.employee.toDomain
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository
import ru.kolesnik.potok.feature.task.navigation.TaskDetailViewRoute
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val projectRepository: FullProjectRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val taskId = savedStateHandle.toRoute<TaskDetailViewRoute>().taskId

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state

    init {
        loadTask()
    }

    fun handleEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskDetailEvent.UpdateTask -> updateTask(event.task, event.onSuccess)
        }
    }

    private fun updateTask(updatedTask: Task, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isSaving = true) }
                val result = taskRepository.updateAndGetTask(updatedTask)
                loadEmployeesForTask(result)
                _state.update { it.copy(task = result) }
                onSuccess()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.toErrorMessage(),
                        isSaving = false
                    )
                }
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                taskRepository.deleteTask(taskId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.toErrorMessage()) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadTask() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val task = taskRepository.getTaskById(taskId)
                task?.let {
                    loadEmployeesForTask(it)
                    _state.update { state ->
                        state.copy(task = it, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.toErrorMessage(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun loadEmployeesForTask(task: Task) {
        val employeeIds = buildList {
            add(task.taskOwner)
            task.assignees?.map { it.employeeId }?.let(::addAll)
        }.distinct()

        try {
            val employees = projectRepository.getEmployee(employeeIds)
            _state.update {
                it.copy(employees = employees.map { it.toDomain() })
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(error = "Ошибка загрузки сотрудников: ${e.message}")
            }
        }
    }

    private fun Exception.toErrorMessage() = when (this) {
        is CancellationException -> null
        else -> "Ошибка: ${localizedMessage ?: "Неизвестная ошибка"}"
    }
}

sealed interface TaskDetailEvent {
    data class DeleteTask(val taskId: String) : TaskDetailEvent
    data class UpdateTask(
        val task: Task,
        val onSuccess: () -> Unit
    ) : TaskDetailEvent
}

data class TaskDetailState(
    val task: Task? = null,
    val employees: List<Employee> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)