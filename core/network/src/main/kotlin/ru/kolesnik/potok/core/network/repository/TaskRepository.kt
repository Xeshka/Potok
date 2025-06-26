package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain

interface TaskRepository {
    fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>>
    fun getTaskMainByFlow(flowId: String): Flow<List<TaskMain>>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun createTask(task: Task): String
    suspend fun updateTask(task: Task)
    suspend fun updateAndGetTask(task: Task): Task
    suspend fun deleteTask(taskId: String)
    suspend fun closeTask(taskId: String, flowId: String)
}