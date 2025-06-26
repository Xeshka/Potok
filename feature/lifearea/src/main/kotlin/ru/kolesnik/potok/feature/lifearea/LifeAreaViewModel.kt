package ru.kolesnik.potok.feature.lifearea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.data.repository.LifeAreaRepository
import ru.kolesnik.potok.core.data.repository.LifeFlowRepository
import ru.kolesnik.potok.core.data.repository.TaskRepository
import ru.kolesnik.potok.core.data.repository.SyncRepository
import ru.kolesnik.potok.core.model.FlowId
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.network.result.Result
import javax.inject.Inject

@HiltViewModel
class LifeAreaViewModel @Inject constructor(
    private val lifeAreaRepository: LifeAreaRepository,
    private val taskRepository: TaskRepository,
    private val lifeFlowRepository: LifeFlowRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val lifeAreas = lifeAreaRepository.getLifeAreas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val flows = lifeAreas
        .flatMapLatest { areas ->
            combine(areas.map { area ->
                lifeFlowRepository.getLifeFlowsByArea(area.id.toString())
                    .map { area.id to it }
            }) { flowList ->
                flowList.associate { it }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    val tasks = lifeAreas
        .flatMapLatest { areas ->
            combine(areas.map { area ->
                taskRepository.getTasksByFlow(area.id.toString()) // Используем существующий метод
                    .map { area.id to it.map { task -> task.toTaskMain() } } // Конвертируем в TaskMain
            }) { taskList ->
                taskList.associate { it }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun deleteTask(taskId: String) = viewModelScope.launch {
        try {
            when (val result = taskRepository.deleteTask(taskId)) {
                is Result.Success -> {
                    // Задача удалена успешно
                }
                is Result.Error -> {
                    _error.value = "Ошибка удаления задачи: ${result.exception.localizedMessage}"
                }
                is Result.Loading -> {
                    // Показываем индикатор загрузки если нужно
                }
            }
        } catch (e: Exception) {
            _error.value = "Ошибка удаления задачи: ${e.localizedMessage}"
        }
    }

    fun createTask(task: Task) = viewModelScope.launch {
        try {
            when (val result = taskRepository.createTask(
                title = task.title,
                description = task.payload?.description,
                lifeFlowId = task.flowId.toString(),
                assigneeIds = task.payload?.assignees ?: emptyList(),
                deadline = task.payload?.deadline?.toString(),
                isImportant = task.payload?.important ?: false
            )) {
                is Result.Success -> {
                    // Задача создана успешно
                }
                is Result.Error -> {
                    _error.value = "Ошибка создания задачи: ${result.exception.localizedMessage}"
                }
                is Result.Loading -> {
                    // Показываем индикатор загрузки если нужно
                }
            }
        } catch (e: Exception) {
            _error.value = "Ошибка создания задачи: ${e.localizedMessage}"
        }
    }

    fun closeTask(taskId: String, flowId: FlowId) = viewModelScope.launch {
        try {
            when (val result = taskRepository.completeTask(taskId)) {
                is Result.Success -> {
                    // Задача завершена успешно
                }
                is Result.Error -> {
                    _error.value = "Ошибка завершения задачи: ${result.exception.localizedMessage}"
                }
                is Result.Loading -> {
                    // Показываем индикатор загрузки если нужно
                }
            }
        } catch (e: Exception) {
            _error.value = "Ошибка завершения задачи: ${e.localizedMessage}"
        }
    }

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                when (val result = syncRepository.syncAll()) {
                    is Result.Success -> {
                        // Синхронизация прошла успешно
                    }
                    is Result.Error -> {
                        _error.value = "Ошибка синхронизации: ${result.exception.localizedMessage}"
                    }
                    is Result.Loading -> {
                        // Уже показываем индикатор загрузки
                    }
                }
            } catch (e: Exception) {
                _error.value = "Ошибка синхронизации: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Расширение для конвертации Task в TaskMain
private fun ru.kolesnik.potok.core.model.Task.toTaskMain(): ru.kolesnik.potok.core.model.TaskMain {
    return ru.kolesnik.potok.core.model.TaskMain(
        id = this.id ?: "",
        title = this.title,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        deadline = this.payload?.deadline,
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = this.assignees ?: emptyList(),
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        lifeAreaId = this.lifeAreaId,
        flowId = this.flowId
    )
}