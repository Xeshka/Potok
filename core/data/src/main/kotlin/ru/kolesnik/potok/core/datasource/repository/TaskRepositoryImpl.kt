package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.entitys.TaskEntity
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.model.TaskPayload
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.api.TaskRq
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.repository.TaskRepository
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao
) : TaskRepository {

    override fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>> {
        val areaUuid = UUID.fromString(lifeAreaId)
        return taskDao.getByAreaIdFlow(areaUuid).map { entities ->
            entities.map { it.toDomainMain() }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val taskUuid = try {
            UUID.fromString(taskId)
        } catch (e: Exception) {
            // Если taskId не является UUID, пробуем найти по externalId
            val task = taskDao.getByExternalId(taskId)
            return task?.toDomain()
        }
        
        val task = taskDao.getById(taskUuid) ?: return null
        return task.toDomain()
    }

    override suspend fun createTask(task: Task): UUID {
        val request = TaskRq(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            payload = ru.kolesnik.potok.core.network.model.api.TaskPayload(
                title = task.title,
                source = task.source,
                onMainPage = task.payload?.onMainPage,
                deadline = task.payload?.deadline,
                lifeArea = task.payload?.lifeArea,
                lifeAreaId = task.lifeAreaId,
                subtitle = task.subtitle,
                userEdit = task.payload?.userEdit,
                assignees = task.payload?.assignees,
                important = task.payload?.important,
                description = task.payload?.description
            )
        )
        
        val response = taskApi.createTask(request)
        val entity = TaskEntity(
            cardId = response.cardId,
            externalId = response.id,
            internalId = response.internalId,
            title = response.title,
            subtitle = response.subtitle,
            mainOrder = response.mainOrder,
            source = response.source,
            taskOwner = response.taskOwner,
            creationDate = response.creationDate,
            payload = task.payload ?: TaskPayload(),
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            lifeAreaPlacement = response.lifeAreaPlacement,
            flowPlacement = response.flowPlacement,
            commentCount = response.commentCount,
            attachmentCount = response.attachmentCount
        )
        
        taskDao.insert(entity)
        
        // Сохраняем исполнителей
        response.assignees?.forEach { assignee ->
            taskAssigneeDao.insert(
                TaskAssigneeEntity(
                    taskCardId = response.cardId,
                    employeeId = assignee.employeeId,
                    complete = assignee.complete
                )
            )
        }
        
        return response.cardId
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        val taskId = task.id ?: throw IllegalArgumentException("Task ID cannot be null")
        
        val patchPayload = PatchPayload(
            title = task.title,
            source = task.source,
            deadline = task.payload?.deadline,
            assignees = task.payload?.assignees,
            important = task.payload?.important,
            description = task.payload?.description,
            priority = task.payload?.priority
        )
        
        taskApi.updateTask(taskId, patchPayload)
        
        // Получаем обновленную задачу с сервера
        val updatedTask = taskApi.getTaskDetails(taskId)
        
        // Обновляем в базе данных
        val entity = TaskEntity(
            cardId = updatedTask.cardId,
            externalId = updatedTask.id,
            internalId = updatedTask.internalId,
            title = updatedTask.title,
            subtitle = updatedTask.subtitle,
            mainOrder = updatedTask.mainOrder,
            source = updatedTask.source,
            taskOwner = updatedTask.taskOwner,
            creationDate = updatedTask.creationDate,
            payload = task.payload ?: TaskPayload(),
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            lifeAreaPlacement = updatedTask.lifeAreaPlacement,
            flowPlacement = updatedTask.flowPlacement,
            commentCount = updatedTask.commentCount,
            attachmentCount = updatedTask.attachmentCount
        )
        
        taskDao.update(entity)
        
        // Обновляем исполнителей
        taskAssigneeDao.deleteByTaskId(updatedTask.cardId)
        updatedTask.assignees?.forEach { assignee ->
            taskAssigneeDao.insert(
                TaskAssigneeEntity(
                    taskCardId = updatedTask.cardId,
                    employeeId = assignee.employeeId,
                    complete = assignee.complete
                )
            )
        }
        
        return entity.toDomain()
    }

    override suspend fun deleteTask(taskId: String) {
        try {
            // Архивируем задачу на сервере
            taskApi.archiveTask(taskId)
            
            // Находим задачу в базе данных
            val taskUuid = try {
                UUID.fromString(taskId)
            } catch (e: Exception) {
                val task = taskDao.getByExternalId(taskId)
                task?.cardId
            }
            
            // Помечаем задачу как удаленную в базе данных
            taskUuid?.let {
                taskDao.markAsArchived(it, OffsetDateTime.now())
            }
        } catch (e: Exception) {
            // Обработка ошибок
            throw e
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        try {
            // Перемещаем задачу в поток "Завершено"
            val request = FlowPositionRq(
                flowId = UUID.fromString(flowId),
                position = 0 // Помещаем в начало списка
            )
            
            taskApi.moveTaskToFlow(taskId, request)
            
            // Обновляем задачу в базе данных
            val taskUuid = try {
                UUID.fromString(taskId)
            } catch (e: Exception) {
                val task = taskDao.getByExternalId(taskId)
                task?.cardId
            }
            
            taskUuid?.let {
                taskDao.moveToFlow(it, UUID.fromString(flowId), 0)
            }
        } catch (e: Exception) {
            // Обработка ошибок
            throw e
        }
    }
    
    private fun TaskEntity.toDomain(): Task = Task(
        id = externalId,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate,
        payload = payload,
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = getAssignees(cardId),
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        checkList = getCheckList(cardId),
        lifeAreaId = lifeAreaId,
        flowId = flowId
    )
    
    private fun TaskEntity.toDomainMain(): TaskMain = TaskMain(
        id = externalId ?: cardId.toString(),
        title = title,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate,
        deadline = payload.deadline,
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = getAssignees(cardId),
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        lifeAreaId = lifeAreaId,
        flowId = flowId
    )
    
    private suspend fun getAssignees(taskId: UUID): List<TaskAssignee> {
        return taskAssigneeDao.getByTaskId(taskId).map {
            TaskAssignee(
                employeeId = it.employeeId,
                complete = it.complete
            )
        }
    }
    
    private suspend fun getCheckList(taskId: UUID): List<ru.kolesnik.potok.core.model.ChecklistTask> {
        return checklistTaskDao.getByTaskId(taskId).map {
            ru.kolesnik.potok.core.model.ChecklistTask(
                id = it.id,
                title = it.title,
                done = it.done ?: false,
                placement = it.placement ?: 0,
                responsibles = it.responsibles,
                deadline = it.deadline
            )
        }
    }
}