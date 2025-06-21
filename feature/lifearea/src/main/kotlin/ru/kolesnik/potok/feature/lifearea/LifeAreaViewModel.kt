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
import ru.kolesnik.potok.core.model.FlowId
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.network.repository.FlowRepository
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository
import javax.inject.Inject

@HiltViewModel
class LifeAreaViewModel @Inject constructor(
    private val projectRepository: FullProjectRepository,
    private val lifeAreaRepository: LifeAreaRepository,
    private val taskRepository: TaskRepository,
    private val flowRepository: FlowRepository
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
                flowRepository.getFlowByLifeArea(area.id.toString())
                    .map { area.id to it }
            }) { taskList ->
                taskList.associate { it }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    var tasks = lifeAreas
        .flatMapLatest { areas ->
            combine(areas.map { area ->
                taskRepository.getTaskMainByArea(area.id.toString())
                    .map { area.id to it }
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
        taskRepository.deleteTask(taskId)
        tasks = lifeAreas
            .flatMapLatest { areas ->
                combine(areas.map { area ->
                    taskRepository.getTaskMainByArea(area.id.toString())
                        .map { area.id to it }
                }) { taskList ->
                    taskList.associate { it }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

    }

    fun createTask(task: Task) = viewModelScope.launch {
        taskRepository.createTask(task)
        tasks = lifeAreas
            .flatMapLatest { areas ->
                combine(areas.map { area ->
                    taskRepository.getTaskMainByArea(area.id.toString())
                        .map { area.id to it }
                }) { taskList ->
                    taskList.associate { it }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )
    }

    fun closeTask(taskId: String, flowId: FlowId) = viewModelScope.launch {
        taskRepository.closeTask(taskId, flowId.toString())
        tasks = lifeAreas
            .flatMapLatest { areas ->
                combine(areas.map { area ->
                    taskRepository.getTaskMainByArea(area.id.toString())
                        .map { area.id to it }
                }) { taskList ->
                    taskList.associate { it }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyMap()
            )

    }

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                projectRepository.sync()
            } catch (e: Exception) {
                _error.value = "Ошибка синхронизации: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}