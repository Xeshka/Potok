package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskApi: TaskApi,
    private val commentApi: CommentApi,
    private val taskDao: TaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getTasksByFlow(flowId: String): Flow<List<Task>> {
        return taskDao.getTasksByFlow(flowId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getTask(id: String): Flow<Task?> {
        return taskDao.getTask(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun syncTasks(): Result<Unit> {
        return try {
            when (val result = taskApi.getTasks()) {
                is Result.Success -> {
                    val entities = result.data.map { it.toEntity() }
                    taskDao.insertAll(entities)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createTask(
        title: String,
        description: String?,
        lifeFlowId: String,
        assigneeIds: List<String>,
        deadline: String?,
        isImportant: Boolean
    ): Result<Task> {
        return try {
            when (val result = taskApi.createTask(title, description, lifeFlowId, assigneeIds, deadline, isImportant)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    taskDao.insert(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
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
            when (val result = taskApi.updateTask(id, title, description, assigneeIds, deadline, isImportant)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    taskDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        return try {
            when (val result = taskApi.deleteTask(id)) {
                is Result.Success -> {
                    taskDao.deleteById(id)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun completeTask(id: String): Result<Task> {
        return try {
            when (val result = taskApi.completeTask(id)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    taskDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun archiveTask(id: String): Result<Unit> {
        return try {
            when (val result = taskApi.archiveTask(id)) {
                is Result.Success -> {
                    taskDao.deleteById(id)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
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
            when (val result = commentApi.addComment(taskId, text)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    taskCommentDao.insert(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateComment(commentId: String, text: String): Result<TaskComment> {
        return try {
            when (val result = commentApi.updateComment(commentId, text)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    taskCommentDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            when (val result = commentApi.deleteComment(commentId)) {
                is Result.Success -> {
                    taskCommentDao.deleteById(commentId)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}