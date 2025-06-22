package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

interface TaskRepository {
    suspend fun createTask(request: TaskRq): UUID
    suspend fun updateTask(taskId: UUID, request: PatchPayload)
    suspend fun moveTaskToFlow(taskId: UUID, request: FlowPositionRq)
    suspend fun moveTaskToLifeArea(taskId: UUID, request: LifeAreaPositionRq)
    suspend fun archiveTask(taskId: UUID)
    suspend fun restoreTask(taskId: UUID)
    suspend fun syncTaskDetails(taskId: UUID)
    suspend fun getTaskDetails(taskId: UUID): TaskEntity?
}

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override suspend fun createTask(request: TaskRq): UUID {
        val response = api.createTask(request)
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        // Сохранение связанных сущностей
        response.assignees?.map { it.toEntity(taskEntity.cardId) }?.let {
            taskAssigneeDao.insertAll(it)
        }
        
        response.checklist?.map { it.toEntity(taskEntity.cardId) }?.let {
            checklistTaskDao.insertAll(it)
        }
        
        return taskEntity.cardId
    }

    override suspend fun updateTask(taskId: UUID, request: PatchPayload) {
        api.updateTask(taskId.toString(), request)
        // После успешного обновления синхронизируем задачу
        syncTaskDetails(taskId)
    }

    override suspend fun moveTaskToFlow(taskId: UUID, request: FlowPositionRq) {
        api.moveTaskToFlow(taskId.toString(), request)
        taskDao.moveToFlow(taskId, request.flowId, request.position)
    }

    override suspend fun moveTaskToLifeArea(taskId: UUID, request: LifeAreaPositionRq) {
        api.moveTaskToLifeArea(taskId.toString(), request)
        taskDao.moveToArea(taskId, request.lifeAreaId, request.position)
    }

    override suspend fun archiveTask(taskId: UUID) {
        api.archiveTask(taskId.toString())
        taskDao.markAsArchived(taskId, OffsetDateTime.now())
    }

    override suspend fun restoreTask(taskId: UUID) {
        api.restoreTasksFromArchive(TaskExternalIds(listOf(taskId.toString())))
        taskDao.restoreFromArchive(taskId)
    }

    override suspend fun syncTaskDetails(taskId: UUID) {
        val response = api.getTaskDetails(taskId.toString())
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        // Обновление связанных сущностей
        taskAssigneeDao.deleteByTaskId(taskId)
        response.assignees?.map { it.toEntity(taskId) }?.let {
            taskAssigneeDao.insertAll(it)
        }
        
        checklistTaskDao.deleteByTaskId(taskId)
        response.checklist?.map { it.toEntity(taskId) }?.let {
            checklistTaskDao.insertAll(it)
        }
        
        // Аналогично для комментариев
    }

    override suspend fun getTaskDetails(taskId: UUID): TaskEntity? {
        return taskDao.getById(taskId)
    }
}

// Мапперы
fun TaskRs.toEntity(): TaskEntity = TaskEntity(
    cardId = UUID.fromString(this.id),
    externalId = this.externalId,
    title = this.title,
    description = this.description,
    // ... остальные поля
)

fun TaskAssigneeDTO.toEntity(taskId: UUID): TaskAssigneeEntity = TaskAssigneeEntity(
    id = UUID.randomUUID(),
    taskCardId = taskId,
    employeeId = this.employeeId,
    complete = this.complete
)
