package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.FlowId
import ru.kolesnik.potok.core.model.LifeAreaId
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import java.util.UUID

interface TaskRepository {
    fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun createTask(task: Task): UUID
    suspend fun updateAndGetTask(task: Task): Task
    suspend fun deleteTask(taskId: String)
    suspend fun closeTask(taskId: String, flowId: String)
}