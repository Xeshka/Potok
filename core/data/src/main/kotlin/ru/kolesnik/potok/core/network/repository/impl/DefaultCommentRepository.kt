package ru.kolesnik.potok.core.network.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.datasource.CommentDataSource
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import ru.kolesnik.potok.core.network.repository.CommentRepository
import ru.kolesnik.potok.core.network.result.Result
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    private val commentDataSource: CommentDataSource,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : CommentRepository {

    override suspend fun getTaskComments(taskId: String, limit: Int, offset: Int): Result<List<TaskComment>> = withContext(ioDispatcher) {
        try {
            val response = commentDataSource.getTaskComments(taskId, limit, offset)
            Result.Success(response.items.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createComment(taskId: String, text: String, parentCommentId: UUID?): Result<TaskComment> = withContext(ioDispatcher) {
        try {
            val request = TaskCommentRq(parentCommentId, text)
            val response = commentDataSource.createComment(taskId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateComment(commentId: UUID, text: String): Result<TaskComment> = withContext(ioDispatcher) {
        try {
            val request = TaskCommentRq(text = text)
            val response = commentDataSource.updateComment(commentId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteComment(commentId: UUID): Result<Unit> = withContext(ioDispatcher) {
        try {
            commentDataSource.deleteComment(commentId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchComments(taskId: String, query: String): Result<List<UUID>> = withContext(ioDispatcher) {
        try {
            val response = commentDataSource.searchComments(taskId, query)
            Result.Success(response.commentIds.map { UUID.fromString(it) })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun TaskCommentDTO.toDomain(): TaskComment {
        return TaskComment(
            id = UUID.fromString(this.id),
            parentCommentId = this.parentCommentId?.let { UUID.fromString(it) },
            owner = this.owner,
            text = this.text,
            createdAt = OffsetDateTime.parse(this.createdAt),
            updatedAt = OffsetDateTime.parse(this.updatedAt)
        )
    }
}