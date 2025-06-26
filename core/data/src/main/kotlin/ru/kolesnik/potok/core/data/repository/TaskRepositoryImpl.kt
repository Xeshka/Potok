package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.TaskRepository
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toTaskMain
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

internal class TaskRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>> {
        return taskDao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { taskEntity ->
                taskEntity.toTaskMain().copy(
                    assignees = taskAssigneeDao.getByTaskId(taskEntity.cardId).map { it.toDomain() }
                )
            }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val taskEntity = taskDao.getByExternalId(taskId) ?: return null
        
        val assignees = taskAssigneeDao.getByTaskId(taskEntity.cardId).map { it.toDomain() }
        val checklist = checklistTaskDao.getByTaskId(taskEntity.cardId).map { it.toDomain() }
        
        return taskEntity.toDomain().copy(
            assignees = assignees,
            checkList = checklist
        )
    }

    override suspend fun createTask(task: Task): Task {
        val networkTask = NetworkCreateTask(
            title = task.title,
            lifeAreaId = task.lifeAreaId?.toString(),
            flowId = task.flowId?.toString(),
            payload = task.payload?.let { payload ->
                ru.kolesnik.potok.core.network.model.potok.NetworkTaskPayload(
                    title = payload.title,
                    description = payload.description,
                    important = payload.important,
                    assignees = payload.assignees
                )
            }
        )
        
        val createdTask = syncDataSource.createTask(networkTask)
        val taskEntity = createdTask.toEntity().copy(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId
        )
        
        taskDao.insert(taskEntity)
        return taskEntity.toDomain()
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val patchPayload = PatchPayload(
            title = task.title,
            description = task.payload?.description,
            important = task.payload?.important,
            deadline = task.payload?.deadline
        )
        
        task.id?.let { taskId ->
            syncDataSource.patchTask(taskId, patchPayload)
        }
        
        // Update local database
        val existingEntity = task.id?.let { taskDao.getByExternalId(it) }
        if (existingEntity != null) {
            val updatedEntity = existingEntity.copy(
                title = task.title,
                subtitle = task.subtitle,
                payload = task.payload ?: existingEntity.payload
            )
            taskDao.update(updatedEntity)
            return updatedEntity.toDomain()
        }
        
        return task
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.getByExternalId(taskId)?.let { entity ->
            taskDao.markAsArchived(entity.cardId, OffsetDateTime.now())
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        taskDao.getByExternalId(taskId)?.let { entity ->
            taskDao.moveToFlow(entity.cardId, UUID.fromString(flowId), 0)
        }
    }

    override suspend fun syncTasksForArea(lifeAreaId: String) {
        try {
            val networkData = syncDataSource.getFull()
            val tasks = networkData
                .find { it.id == lifeAreaId }
                ?.flows
                ?.flatMap { flow -> 
                    flow.tasks?.map { task ->
                        task.toEntity().copy(
                            lifeAreaId = UUID.fromString(lifeAreaId),
                            flowId = UUID.fromString(flow.id)
                        )
                    } ?: emptyList()
                }
                ?: emptyList()
            
            // Clear existing tasks for this area and insert new ones
            taskDao.deleteByAreaId(UUID.fromString(lifeAreaId))
            taskDao.insertAll(tasks)
            
            // Sync related data (assignees, checklist, etc.)
            tasks.forEach { taskEntity ->
                // This would need to be implemented based on the network data structure
                // For now, we'll skip the detailed sync
            }
        } catch (e: Exception) {
            println("Failed to sync tasks for area $lifeAreaId: ${e.message}")
        }
    }
}