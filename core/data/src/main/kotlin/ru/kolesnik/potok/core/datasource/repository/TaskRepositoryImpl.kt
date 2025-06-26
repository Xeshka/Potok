package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.TaskRepository
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.model.extensions.toEntity
import ru.kolesnik.potok.core.model.extensions.toRequest
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao
) : TaskRepository {

    override fun getTaskMainByArea(areaId: String): Flow<List<TaskMain>> {
        val areaUuid = UUID.fromString(areaId)
        return taskDao.getByAreaId(areaUuid).map { entities ->
            entities.map { entity ->
                val assignees = taskAssigneeDao.getByTaskId(entity.cardId)
                entity.toDomain(assignees.map { it.toDomain() })
            }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val taskUuid = UUID.fromString(taskId)
        val entity = taskDao.getById(taskUuid) ?: return null
        val assignees = taskAssigneeDao.getByTaskId(taskUuid)
        return entity.toDomain(assignees.map { it.toDomain() })
    }

    override suspend fun createTask(task: Task): String {
        val request = task.toRequest()
        val response = api.createTask(request)
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        response.assignees?.forEach { assigneeDto ->
            val assigneeEntity = assigneeDto.toEntity(taskEntity.cardId)
            taskAssigneeDao.insert(assigneeEntity)
        }
        
        return response.id
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val taskId = task.id ?: throw IllegalArgumentException("Task ID cannot be null for update")
        val patchPayload = PatchPayload(
            title = task.title,
            description = task.payload?.description,
            important = task.payload?.important,
            deadline = task.payload?.deadline
        )
        
        api.updateTask(taskId, patchPayload)
        
        // Получаем обновленную задачу с сервера
        val updatedResponse = api.getTaskDetails(taskId)
        val updatedEntity = updatedResponse.toEntity()
        taskDao.insert(updatedEntity)
        
        // Обновляем исполнителей
        taskAssigneeDao.deleteByTaskId(updatedEntity.cardId)
        updatedResponse.assignees?.forEach { assigneeDto ->
            val assigneeEntity = assigneeDto.toEntity(updatedEntity.cardId)
            taskAssigneeDao.insert(assigneeEntity)
        }
        
        val assignees = taskAssigneeDao.getByTaskId(updatedEntity.cardId)
        return updatedEntity.toDomain(assignees.map { it.toDomain() })
    }

    override suspend fun deleteTask(taskId: String) {
        api.archiveTask(taskId)
        val taskUuid = UUID.fromString(taskId)
        taskDao.getById(taskUuid)?.let { entity ->
            taskDao.delete(entity)
            taskAssigneeDao.deleteByTaskId(taskUuid)
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        val flowUuid = UUID.fromString(flowId)
        val request = FlowPositionRq(flowId = flowUuid, position = 0)
        api.moveTaskToFlow(taskId, request)
        
        val taskUuid = UUID.fromString(taskId)
        taskDao.moveToFlow(taskUuid, flowUuid, 0)
    }

    override suspend fun syncTasks() {
        // Синхронизация задач будет выполняться через FullProjectRepository
    }
}