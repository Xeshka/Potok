package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.api.TaskRq
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

interface TaskRepository {
    fun getTaskMainByArea(areaId: String): Flow<List<TaskMain>>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun createTask(task: Task): UUID
    suspend fun updateTask(taskId: UUID, request: PatchPayload)
    suspend fun updateAndGetTask(task: Task): Task
    suspend fun deleteTask(taskId: String)
    suspend fun closeTask(taskId: String, flowId: String)
}

class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override fun getTaskMainByArea(areaId: String): Flow<List<TaskMain>> = flow {
        val areaUuid = UUID.fromString(areaId)
        val tasks = taskDao.getByAreaId(areaUuid).map { entity ->
            TaskMain(
                id = entity.externalId ?: entity.cardId.toString(),
                title = entity.title,
                source = entity.source,
                taskOwner = entity.taskOwner,
                creationDate = entity.creationDate,
                deadline = entity.payload.deadline,
                internalId = entity.internalId,
                lifeAreaPlacement = entity.lifeAreaPlacement,
                flowPlacement = entity.flowPlacement,
                assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { assignee ->
                    TaskAssignee(
                        employeeId = assignee.employeeId,
                        complete = assignee.complete
                    )
                },
                commentCount = entity.commentCount,
                attachmentCount = entity.attachmentCount,
                lifeAreaId = entity.lifeAreaId,
                flowId = entity.flowId
            )
        }
        emit(tasks)
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val uuid = try {
            UUID.fromString(taskId)
        } catch (e: Exception) {
            // Если taskId не является UUID, пробуем найти по externalId
            val task = taskDao.getByExternalId(taskId)
            return task?.let { mapEntityToTask(it) }
        }
        
        val entity = taskDao.getById(uuid) ?: return null
        return mapEntityToTask(entity)
    }

    private suspend fun mapEntityToTask(entity: ru.kolesnik.potok.core.database.entitys.TaskEntity): Task {
        val assignees = taskAssigneeDao.getByTaskId(entity.cardId).map {
            TaskAssignee(
                employeeId = it.employeeId,
                complete = it.complete
            )
        }
        
        val checkList = checklistTaskDao.getByTaskId(entity.cardId).map {
            ru.kolesnik.potok.core.model.ChecklistTask(
                id = it.id,
                title = it.title,
                done = it.done,
                placement = it.placement,
                responsibles = it.responsibles,
                deadline = it.deadline
            )
        }
        
        return Task(
            id = entity.externalId,
            title = entity.title,
            subtitle = entity.subtitle,
            mainOrder = entity.mainOrder,
            source = entity.source,
            taskOwner = entity.taskOwner,
            creationDate = entity.creationDate,
            payload = entity.payload,
            internalId = entity.internalId,
            lifeAreaPlacement = entity.lifeAreaPlacement,
            flowPlacement = entity.flowPlacement,
            assignees = assignees,
            commentCount = entity.commentCount,
            attachmentCount = entity.attachmentCount,
            checkList = checkList,
            lifeAreaId = entity.lifeAreaId,
            flowId = entity.flowId
        )
    }

    override suspend fun createTask(task: Task): UUID {
        val request = TaskRq(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            payload = task.payload ?: ru.kolesnik.potok.core.model.TaskPayload(
                title = task.title,
                description = "",
                important = false,
                assignees = emptyList()
            )
        )
        
        val response = taskApi.createTask(request)
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        // Сохраняем связанные сущности
        response.assignees?.map { it.toEntity(taskEntity.cardId) }?.let {
            taskAssigneeDao.insertAll(it)
        }
        
        response.checkList?.map { it.toChecklistEntity(taskEntity.cardId) }?.let {
            checklistTaskDao.insertAll(it)
        }
        
        return taskEntity.cardId
    }

    override suspend fun updateTask(taskId: UUID, request: PatchPayload) {
        taskApi.updateTask(taskId.toString(), request)
        // После успешного обновления синхронизируем задачу
        val response = taskApi.getTaskDetails(taskId.toString())
        val taskEntity = response.toEntity()
        taskDao.insert(taskEntity)
        
        // Обновляем связанные сущности
        taskAssigneeDao.deleteByTaskId(taskId)
        response.assignees?.map { it.toEntity(taskId) }?.let {
            taskAssigneeDao.insertAll(it)
        }
        
        checklistTaskDao.deleteByTaskId(taskId)
        response.checkList?.map { it.toChecklistEntity(taskId) }?.let {
            checklistTaskDao.insertAll(it)
        }
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val taskId = task.id?.let { UUID.fromString(it) } ?: task.payload?.externalId?.let { UUID.fromString(it) }
        
        if (taskId != null) {
            val patchPayload = PatchPayload(
                title = task.title,
                description = task.payload?.description,
                deadline = task.payload?.deadline,
                important = task.payload?.important,
                assignees = task.payload?.assignees
            )
            
            updateTask(taskId, patchPayload)
            return getTaskById(taskId.toString()) ?: task
        }
        
        return task
    }

    override suspend fun deleteTask(taskId: String) {
        try {
            val uuid = UUID.fromString(taskId)
            taskApi.archiveTask(taskId)
            taskDao.markAsArchived(uuid, OffsetDateTime.now())
        } catch (e: Exception) {
            // Обработка ошибки
            throw e
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        try {
            val taskUuid = UUID.fromString(taskId)
            val flowUuid = UUID.fromString(flowId)
            
            // Перемещаем задачу в поток "Завершено"
            val request = FlowPositionRq(
                flowId = flowUuid,
                position = 1 // Помещаем в начало списка завершенных задач
            )
            
            taskApi.moveTaskToFlow(taskId, request)
            taskDao.moveToFlow(taskUuid, flowUuid, 1)
            
            // Отмечаем всех исполнителей как завершивших задачу
            val assignees = taskAssigneeDao.getByTaskId(taskUuid)
            assignees.forEach { assignee ->
                taskAssigneeDao.updateCompletionStatus(taskUuid, assignee.employeeId, true)
            }
        } catch (e: Exception) {
            // Обработка ошибки
            throw e
        }
    }
}

// Маппер из DTO в Entity
fun ru.kolesnik.potok.core.network.model.api.TaskRs.toEntity(): ru.kolesnik.potok.core.database.entitys.TaskEntity {
    return ru.kolesnik.potok.core.database.entitys.TaskEntity(
        cardId = cardId,
        externalId = id,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate,
        payload = payload.toDomainModel(),
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        lifeAreaId = payload.lifeAreaId,
        flowId = null // Нужно заполнить из контекста
    )
}

fun ru.kolesnik.potok.core.network.model.api.TaskPayload.toDomainModel(): ru.kolesnik.potok.core.model.TaskPayload {
    return ru.kolesnik.potok.core.model.TaskPayload(
        title = title,
        source = source,
        onMainPage = onMainPage,
        deadline = deadline,
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId,
        subtitle = subtitle,
        userEdit = userEdit,
        assignees = assignees,
        important = important,
        messageId = messageId,
        fullMessage = fullMessage,
        description = description,
        priority = priority,
        userChangeAssignee = userChangeAssignee,
        organization = organization,
        shortMessage = shortMessage,
        externalId = externalId,
        relatedAssignment = relatedAssignment,
        meanSource = meanSource
    )
}

fun ru.kolesnik.potok.core.network.model.api.TaskAssigneeRs.toEntity(taskId: UUID): ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity {
    return ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity(
        taskCardId = taskId,
        employeeId = employeeId,
        complete = complete
    )
}

fun ru.kolesnik.potok.core.network.model.api.ChecklistTaskDTO.toChecklistEntity(taskId: UUID): ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity {
    return ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity(
        id = id,
        taskCardId = taskId,
        title = title,
        done = done ?: false,
        placement = placement ?: 0,
        responsibles = responsibles,
        deadline = deadline
    )
}