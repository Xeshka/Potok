package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.data.util.toTaskMain
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitCommentDataSource
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitTaskDataSource
import ru.kolesnik.potok.core.network.model.LifeAreaId
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(

    private val taskApi: RetrofitTaskDataSource,
    private val commentApi: RetrofitCommentDataSource,
    private val taskDao: TaskDao,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskCommentDao: TaskCommentDao,
    private val taskAssigneeDao: TaskAssigneeDao
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { entity ->
                val assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { it.toModel() }
                entity.toModel().copy(assignees = assignees)
            }
        }
    }

    override fun getTasksByFlow(flowId: String): Flow<List<Task>> {
        return taskDao.getTasksByFlow(flowId).map { entities ->
            entities.map { entity ->
                val assignees = taskAssigneeDao.getByTaskId(entity.cardId).map { it.toModel() }
                entity.toModel().copy(assignees = assignees)
            }
        }
    }

    override fun getTask(id: String): Flow<Task?> {
        return taskDao.getTask(id).map { entity ->
            entity?.let {
                val assignees = taskAssigneeDao.getByTaskId(it.cardId).map { it.toModel() }
                it.toModel().copy(assignees = assignees)
            }
        }
    }

    override suspend fun syncTasks(): Result<Unit> {
        return try {
            // Здесь должна быть логика синхронизации всех задач
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createTask(
        title: String,
        description: String?,
        lifeAreaId: LifeAreaId?,
        lifeFlowId: String,
        assigneeIds: List<String>,
        deadline: String?,
        isImportant: Boolean
    ): Result<Task> {
        return try {
            val payload = ru.kolesnik.potok.core.network.model.api.TaskPayload(
                title = title,
                description = description,
                important = isImportant,
                assignees = assigneeIds,
                deadline = deadline?.let { java.time.OffsetDateTime.parse(it) }
            )
            val request = ru.kolesnik.potok.core.network.model.api.TaskRq(
                flowId = java.util.UUID.fromString(lifeFlowId),
                payload = payload
            )
            val result = taskApi.createTask(request)

            val lifeArea =
                lifeAreaId?.let { UUID.fromString(it) } ?: lifeAreaDao.getAllLifeAreas().first()
                    .first { it.placement == 1 }.id
            val lifeFlow =
                lifeAreaId?.let { UUID.fromString(it) } ?: lifeFlowDao.getByAreaIdFlow(lifeArea)
                    .first().first { it.placement == 1 }.id

            val entity = result.toEntity(lifeArea, lifeFlow)
            taskDao.insert(entity)

            // Сохраняем назначенных
            assigneeIds.forEach { assigneeId ->
                val assigneeEntity = ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity(
                    taskCardId = entity.cardId,
                    employeeId = assigneeId,
                    complete = false
                )
                taskAssigneeDao.insert(assigneeEntity)
            }

            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateTask(
        id: String,
        title: String?,
        description: String?,
        assigneeIds: List<String>?,
        deadline: String?,
        isImportant: Boolean?
    ): Result<Task> {
        return try {
            val payload = ru.kolesnik.potok.core.network.model.potok.PatchPayload(
                title = title,
                description = description,
                important = isImportant,
                assignees = assigneeIds,
                deadline = deadline?.let { java.time.OffsetDateTime.parse(it) }
            )
            taskApi.updateTask(id, payload)

            // Обновляем локальную базу
            val entity = taskDao.getByExternalId(id)
            entity?.let {
                val updatedEntity = it.copy(
                    title = title ?: it.title,
                    payload = it.payload?.copy(
                        description = description ?: it.payload?.description,
                        important = isImportant ?: it.payload?.important
                    )
                )
                taskDao.update(updatedEntity)
                Result.Success(updatedEntity.toModel())
            } ?: Result.Error(Exception("Task not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        return try {
            taskApi.archiveTask(id)
            taskDao.deleteById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun completeTask(id: String): Result<Task> {
        return try {
            val task = taskDao.getByExternalId(id)
            val result = if (task != null) {
                val flow = lifeFlowDao.getLifeFlowsByArea(task.lifeAreaId!!.toString()).first()
                    .sortedBy { it.placement }.last().id
                taskApi.moveTaskToFlow(id, FlowPositionRq(flow, 0))
                val result = taskApi.getTaskDetails(id)

                val entity = result.toEntity(task.lifeAreaId!!, flow)
                taskDao.update(entity)
                entity
            } else throw Exception("NotComplete")
            Result.Success(result.toModel())

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun archiveTask(id: String): Result<Unit> {
        return try {
            taskApi.archiveTask(id)
            taskDao.deleteById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Comments
    override fun getTaskComments(taskId: String): Flow<List<TaskComment>> {
        return taskCommentDao.getCommentsByTask(taskId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun addComment(taskId: String, text: String): Result<TaskComment> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.TaskCommentRq(text = text)
            val result = commentApi.createComment(taskId, request)
            val entity = result.toEntity().copy(taskCardId = java.util.UUID.fromString(taskId))
            taskCommentDao.insert(entity)
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateComment(commentId: String, text: String): Result<TaskComment> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.TaskCommentRq(text = text)
            val result = commentApi.updateComment(java.util.UUID.fromString(commentId), request)
            val entity = result.toEntity()
            taskCommentDao.update(entity)
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            commentApi.deleteComment(java.util.UUID.fromString(commentId))
            taskCommentDao.deleteById(java.util.UUID.fromString(commentId))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}