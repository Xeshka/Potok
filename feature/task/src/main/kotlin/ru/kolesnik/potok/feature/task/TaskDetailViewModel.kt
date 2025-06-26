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
import ru.kolesnik.potok.core.data.repository.TaskRepository
import ru.kolesnik.potok.core.data.repository.CommentRepository
import ru.kolesnik.potok.core.data.repository.SyncRepository
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.feature.task.navigation.TaskDetailViewRoute
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val commentRepository: CommentRepository,
    private val syncRepository: SyncRepository,
) : ViewModel() {

    private val taskId = savedStateHandle.toRoute<TaskDetailViewRoute>().taskId

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state

    init {
        loadTask()
        loadComments()
    }

    fun handleEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.DeleteTask -> deleteTask(event.taskId)
            is TaskDetailEvent.UpdateTask -> updateTask(event.task, event.onSuccess)
            is TaskDetailEvent.AddComment -> addComment(event.text)
            is TaskDetailEvent.UpdateComment -> updateComment(event.commentId, event.text)
            is TaskDetailEvent.DeleteComment -> deleteComment(event.commentId)
            is TaskDetailEvent.LoadComments -> loadComments()
        }
    }

    private fun updateTask(updatedTask: Task, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isSaving = true) }
                
                when (val result = taskRepository.updateTask(
                    id = updatedTask.id ?: return@launch,
                    title = updatedTask.title,
                    description = updatedTask.payload?.description,
                    assigneeIds = updatedTask.payload?.assignees,
                    deadline = updatedTask.payload?.deadline?.toString(),
                    isImportant = updatedTask.payload?.important
                )) {
                    is Result.Success -> {
                        _state.update { it.copy(task = result.data) }
                        onSuccess()
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = result.exception.toErrorMessage()) }
                    }
                    is Result.Loading -> {
                        // Уже обрабатывается через isSaving
                    }
                }
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
                
                when (val result = taskRepository.deleteTask(taskId)) {
                    is Result.Success -> {
                        _state.update { it.copy(task = null) }
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = result.exception.toErrorMessage()) }
                    }
                    is Result.Loading -> {
                        // Уже обрабатывается через isLoading
                    }
                }
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
                
                taskRepository.getTask(taskId).collect { task ->
                    _state.update { state ->
                        state.copy(
                            task = task,
                            isLoading = false,
                            error = if (task == null) "Задача не найдена" else null
                        )
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

    private fun loadComments() {
        viewModelScope.launch {
            try {
                commentRepository.getTaskComments(taskId).collect { comments ->
                    _state.update { it.copy(comments = comments) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Ошибка загрузки комментариев: ${e.message}")
                }
            }
        }
    }

    private fun addComment(text: String) {
        viewModelScope.launch {
            try {
                when (val result = commentRepository.addComment(taskId, text)) {
                    is Result.Success -> {
                        // Комментарий автоматически появится через Flow
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = "Ошибка добавления комментария: ${result.exception.message}") }
                    }
                    is Result.Loading -> {
                        // Можно показать индикатор загрузки
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Ошибка добавления комментария: ${e.message}") }
            }
        }
    }

    private fun updateComment(commentId: String, text: String) {
        viewModelScope.launch {
            try {
                when (val result = commentRepository.updateComment(commentId, text)) {
                    is Result.Success -> {
                        // Комментарий автоматически обновится через Flow
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = "Ошибка обновления комментария: ${result.exception.message}") }
                    }
                    is Result.Loading -> {
                        // Можно показать индикатор загрузки
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Ошибка обновления комментария: ${e.message}") }
            }
        }
    }

    private fun deleteComment(commentId: String) {
        viewModelScope.launch {
            try {
                when (val result = commentRepository.deleteComment(commentId)) {
                    is Result.Success -> {
                        // Комментарий автоматически удалится через Flow
                    }
                    is Result.Error -> {
                        _state.update { it.copy(error = "Ошибка удаления комментария: ${result.exception.message}") }
                    }
                    is Result.Loading -> {
                        // Можно показать индикатор загрузки
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Ошибка удаления комментария: ${e.message}") }
            }
        }
    }

    private fun Exception.toErrorMessage() = when (this) {
        is CancellationException -> null
        else -> "Ошибка: ${localizedMessage ?: "Неизвестная ошибка"}"
    }

    private fun Throwable.toErrorMessage() = when (this) {
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
    data class AddComment(val text: String) : TaskDetailEvent
    data class UpdateComment(val commentId: String, val text: String) : TaskDetailEvent
    data class DeleteComment(val commentId: String) : TaskDetailEvent
    data object LoadComments : TaskDetailEvent
}

data class TaskDetailState(
    val task: Task? = null,
    val comments: List<TaskComment> = emptyList(),
    val employees: List<Employee> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)