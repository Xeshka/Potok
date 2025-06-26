package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.api.TaskRq
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.repository.TaskRepository
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao,
    private val syncRepository: SyncRepository
) : TaskRepository {

    override fun getTaskMainByArea(lifeAreaId: String): Flow<List<TaskMain>> {
        return taskDao.getByAreaIdFlow(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { entity ->
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
                    assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { 
                        ru.kolesnik.potok.core.model.TaskAssignee(
                            employeeId = it.employeeId,
                            complete = it.complete
                        )
                    },
                    commentCount = entity.commentCount,
                    attachmentCount = entity.attachmentCount,
                    lifeAreaId = entity.lifeAreaId,
                    flowId = entity.flowId
                )
            }
        }
    }

    override fun getTaskMainByFlow(flowId: String): Flow<List<TaskMain>> {
        return taskDao.getByFlowIdFlow(UUID.fromString(flowId)).map { entities ->
            entities.map { entity ->
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
                    assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { 
                        ru.kolesnik.potok.core.model.TaskAssignee(
                            employeeId = it.employeeId,
                            complete = it.complete
                        )
                    },
                    commentCount = entity.commentCount,
                    attachmentCount = entity.attachmentCount,
                    lifeAreaId = entity.lifeAreaId,
                    flowId = entity.flowId
                )
            }
        }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val entity = taskDao.getByExternalId(taskId) ?: return null
        
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
            assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { 
                ru.kolesnik.potok.core.model.TaskAssignee(
                    employeeId = it.employeeId,
                    complete = it.complete
                )
            },
            commentCount = entity.commentCount,
            attachmentCount = entity.attachmentCount,
            checkList = checklistTaskDao.getByTaskId(entity.cardId).map {
                ru.kolesnik.potok.core.model.ChecklistTask(
                    id = it.id,
                    title = it.title,
                    done = it.done ?: false,
                    placement = it.placement ?: 0,
                    responsibles = it.responsibles,
                    deadline = it.deadline
                )
            },
            lifeAreaId = entity.lifeAreaId,
            flowId = entity.flowId
        )
    }

    override suspend fun createTask(task: Task): String {
        val request = TaskRq(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            payload = task.payload ?: ru.kolesnik.potok.core.network.model.api.TaskPayload(
                title = task.title,
                assignees = emptyList()
            )
        )
        
        val response = api.createTask(request)
        
        // Синхронизируем задачи для потока, в который добавили задачу
        if (task.flowId != null) {
            syncRepository.syncTasks(task.flowId!!)
        }
        
        return response.id
    }

    override suspend fun updateTask(task: Task) {
        val patchPayload = PatchPayload(
            title = task.title,
            description = task.payload?.description,
            deadline = task.payload?.deadline,
            important = task.payload?.important,
            assignees = task.payload?.assignees
        )
        
        api.updateTask(task.id!!, patchPayload)
        
        // Обновляем задачу в локальной базе данных
        val entity = taskDao.getByExternalId(task.id!!)
        if (entity != null) {
            val updatedEntity = entity.copy(
                title = task.title,
                payload = entity.payload.copy(
                    title = task.title,
                    description = task.payload?.description,
                    deadline = task.payload?.deadline,
                    important = task.payload?.important,
                    assignees = task.payload?.assignees
                )
            )
            taskDao.update(updatedEntity)
        }
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        updateTask(task)
        
        // Получаем обновленную задачу с сервера
        val response = api.getTaskDetails(task.id!!)
        
        // Обновляем задачу в локальной базе данных
        val entity = taskDao.getByExternalId(task.id!!)
        if (entity != null) {
            val updatedEntity = entity.copy(
                title = response.title,
                subtitle = response.subtitle,
                payload = response.payload,
                commentCount = response.commentCount,
                attachmentCount = response.attachmentCount
            )
            taskDao.update(updatedEntity)
            
            // Обновляем связанные сущности
            response.assignees?.let { assignees ->
                taskAssigneeDao.deleteByTaskId(entity.cardId)
                assignees.forEach { assignee ->
                    taskAssigneeDao.insert(
                        ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity(
                            taskCardId = entity.cardId,
                            employeeId = assignee.employeeId,
                            complete = assignee.complete
                        )
                    )
                }
            }
            
            response.checkList?.let { checkList ->
                checklistTaskDao.deleteByTaskId(entity.cardId)
                checkList.forEach { checkItem ->
                    checklistTaskDao.insert(
                        ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity(
                            id = checkItem.id,
                            taskCardId = entity.cardId,
                            title = checkItem.title,
                            done = checkItem.done,
                            placement = checkItem.placement,
                            responsibles = checkItem.responsibles,
                            deadline = checkItem.deadline
                        )
                    )
                }
            }
        }
        
        return getTaskById(task.id!!)!!
    }

    override suspend fun deleteTask(taskId: String) {
        val entity = taskDao.getByExternalId(taskId)
        if (entity != null) {
            // Здесь должен быть вызов API для удаления задачи, но в API нет такого метода
            // Поэтому просто удаляем из локальной базы данных
            taskDao.delete(entity)
        }
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        // Перемещаем задачу в поток "Завершено"
        val request = FlowPositionRq(
            flowId = UUID.fromString(flowId),
            position = 0 // Помещаем в начало списка
        )
        
        api.moveTaskToFlow(taskId, request)
        
        // Обновляем задачу в локальной базе данных
        val entity = taskDao.getByExternalId(taskId)
        if (entity != null) {
            taskDao.moveToFlow(entity.cardId, UUID.fromString(flowId), 0)
        }
    }
}