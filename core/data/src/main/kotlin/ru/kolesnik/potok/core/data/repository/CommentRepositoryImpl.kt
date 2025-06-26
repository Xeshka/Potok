package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.TaskCommentDao
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitCommentDataSource
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val commentApi: RetrofitCommentDataSource,
    private val taskCommentDao: TaskCommentDao
) : CommentRepository {

    override fun getTaskComments(taskId: String, limit: Int, offset: Int): Flow<List<TaskComment>> {
        return taskCommentDao.getCommentsByTask(taskId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun createComment(taskId: String, text: String, parentCommentId: String?): Result<TaskComment> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.TaskCommentRq(
                text = text,
                parentCommentId = parentCommentId?.let { UUID.fromString(it) }
            )
            
            val result = commentApi.createComment(taskId, request)
            val entity = result.toEntity().copy(
                taskCardId = UUID.fromString(taskId)
            )
            taskCommentDao.insert(entity)
            
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getComment(commentId: String): Result<TaskComment> {
        return try {
            val uuid = UUID.fromString(commentId)
            
            // Сначала пытаемся получить из локальной базы
            val localComment = taskCommentDao.getById(uuid)
            if (localComment != null) {
                return Result.Success(localComment.toModel())
            }
            
            // Если нет в локальной базе, получаем с сервера
            val result = commentApi.getComment(uuid)
            val entity = result.toEntity()
            taskCommentDao.insert(entity)
            
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateComment(commentId: String, text: String): Result<TaskComment> {
        return try {
            val uuid = UUID.fromString(commentId)
            val request = ru.kolesnik.potok.core.network.model.api.TaskCommentRq(text = text)
            
            val result = commentApi.updateComment(uuid, request)
            val entity = result.toEntity()
            taskCommentDao.update(entity)
            
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            val uuid = UUID.fromString(commentId)
            
            commentApi.deleteComment(uuid)
            taskCommentDao.deleteById(uuid)
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchComments(taskId: String, query: String): Result<List<String>> {
        return try {
            val result = commentApi.searchComments(taskId, query)
            val commentIds = result.commentIds.map { it.toString() }
            
            Result.Success(commentIds)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCommentCount(taskId: String): Int {
        return try {
            val taskUuid = UUID.fromString(taskId)
            taskCommentDao.getCommentCount(taskUuid)
        } catch (e: Exception) {
            0
        }
    }

    // Дополнительные методы для работы с комментариями
    suspend fun getReplies(parentCommentId: String): Result<List<TaskComment>> {
        return try {
            val uuid = UUID.fromString(parentCommentId)
            val replies = taskCommentDao.getReplies(uuid)
            Result.Success(replies.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCommentsByOwner(ownerId: String): Result<List<TaskComment>> {
        return try {
            val comments = taskCommentDao.getByOwner(ownerId)
            Result.Success(comments.map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun syncTaskComments(taskId: String): Result<Unit> {
        return try {
            val response = commentApi.getTaskComments(taskId)
            val entities = response.items.map { comment ->
                comment.toEntity().copy(taskCardId = UUID.fromString(taskId))
            }
            
            // Удаляем старые комментарии для этой задачи
            taskCommentDao.deleteByTaskId(UUID.fromString(taskId))
            
            // Вставляем новые
            taskCommentDao.insertAll(entities)
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}