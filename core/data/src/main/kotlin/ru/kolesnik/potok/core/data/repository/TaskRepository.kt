package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toTaskMain
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface TaskRepository {
    fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun createTask(task: Task): UUID
    suspend fun updateAndGetTask(task: Task): Task
    suspend fun deleteTask(taskId: String)
    suspend fun closeTask(taskId: String, flowId: String)
    suspend fun moveTaskToFlow(taskId: UUID, request: FlowPositionRq)
    suspend fun moveTaskToLifeArea(taskId: UUID, request: LifeAreaPositionRq)
    suspend fun archiveTask(taskId: UUID)
    suspend fun restoreTask(taskId: UUID)
    suspend fun syncTaskDetails(taskId: UUID)
}

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>> {
        return taskDao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toTaskMain() }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return taskDao.getByExternalId(taskId)?.toDomain()
    }

    override suspend fun createTask(task: Task): UUID {
        val request = TaskRq(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            payload = TaskPayload(
                title = task.title,
                description = task.payload?.description,
                important = task.payload?.important,
                assignees = task.assignees?.map { it.employeeId }
            )
        )
        
        val response = api.createTask(request)
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        // Сохранение связанных сущностей
        response.assignees?.map { it.toEntity(taskEntity.cardId) }?.let {
            taskAssigneeDao.insertAll(it)
        }
        
        response.checkList?.map { it.toEntity(taskEntity.cardId) }?.let {
            checklistTaskDao.insertAll(it)
        }
        
        return taskEntity.cardId
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val patchPayload = PatchPayload(
            title = task.title,
            description = task.payload?.description,
            important = task.payload?.important,
            deadline = task.payload?.deadline
        )
        
        api.updateTask(task.id!!, patchPayload)
        
        // Синхронизируем обновленную задачу
        syncTaskDetails(UUID.fromString(task.id))
        
        return getTaskById(task.id) ?: task
    }

    override suspend fun deleteTask(taskId: String) {
        // Помечаем как удаленную в БД
        taskDao.markAsArchived(UUID.fromString(taskId), OffsetDateTime.now())
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        val request = FlowPositionRq(
            flowId = UUID.fromString(flowId),
            position = 0 // Позиция в завершенных задачах
        )
        api.moveTaskToFlow(taskId, request)
        taskDao.moveToFlow(UUID.fromString(taskId), UUID.fromString(flowId), 0)
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
        response.checkList?.map { it.toEntity(taskId) }?.let {
            checklistTaskDao.insertAll(it)
        }
    }
}