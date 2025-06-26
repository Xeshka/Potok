package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.util.*
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val assigneeDao: TaskAssigneeDao,
    private val checklistDao: ChecklistTaskDao,
    private val commentDao: TaskCommentDao
) : TaskRepository {

    override fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>> {
        return taskDao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toTaskMain() }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val entity = taskDao.getByExternalId(taskId) ?: return null
        val assignees = assigneeDao.getByTaskId(entity.cardId).map { it.toDomain() }
        val checklist = checklistDao.getByTaskId(entity.cardId).map { it.toDomain() }
        
        return entity.toDomain().copy(
            assignees = assignees,
            checkList = checklist
        )
    }

    override suspend fun createTask(task: Task): String {
        val request = task.toTaskRq()
        val response = api.createTask(request)
        val taskEntity = response.toEntity()
        
        taskDao.insert(taskEntity)
        
        // Save assignees
        response.assignees?.forEach { assigneeDto ->
            val assigneeEntity = assigneeDto.toEntity(taskEntity.cardId)
            assigneeDao.insert(assigneeEntity)
        }
        
        // Save checklist
        response.checkList?.forEach { checklistDto ->
            val checklistEntity = checklistDto.toEntity(taskEntity.cardId)
            checklistDao.insert(checklistEntity)
        }
        
        return response.id
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val payload = PatchPayload(
            title = task.title,
            description = task.payload?.description,
            important = task.payload?.important,
            deadline = task.payload?.deadline
        )
        
        api.updateTask(task.id ?: task.taskOwner, payload)
        
        // Sync task details after update
        syncTaskDetails(task.id ?: task.taskOwner)
        
        return getTaskById(task.id ?: task.taskOwner) ?: task
    }

    override suspend fun deleteTask(taskId: String) {
        // Archive task instead of deleting
        api.archiveTask(taskId)
        taskDao.getByExternalId(taskId)?.let { entity ->
            taskDao.markAsArchived(entity.cardId, java.time.OffsetDateTime.now())
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        val request = FlowPositionRq(
            flowId = UUID.fromString(flowId),
            position = 0 // Or calculate proper position
        )
        api.moveTaskToFlow(taskId, request)
        
        // Update local database
        taskDao.moveToFlow(
            taskId = UUID.fromString(taskId),
            flowId = UUID.fromString(flowId),
            position = 0
        )
    }

    private suspend fun syncTaskDetails(taskId: String) {
        try {
            val response = api.getTaskDetails(taskId)
            val taskEntity = response.toEntity()
            taskDao.insert(taskEntity)
            
            // Update assignees
            assigneeDao.deleteByTaskId(taskEntity.cardId)
            response.assignees?.forEach { assigneeDto ->
                val assigneeEntity = assigneeDto.toEntity(taskEntity.cardId)
                assigneeDao.insert(assigneeEntity)
            }
            
            // Update checklist
            checklistDao.deleteByTaskId(taskEntity.cardId)
            response.checkList?.forEach { checklistDto ->
                val checklistEntity = checklistDto.toEntity(taskEntity.cardId)
                checklistDao.insert(checklistEntity)
            }
        } catch (e: Exception) {
            // Handle sync error
            throw e
        }
    }
}