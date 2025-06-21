package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.result.Result
import java.util.UUID

interface CommentRepository {
    suspend fun getTaskComments(taskId: String, limit: Int = 10, offset: Int = 0): Result<List<TaskComment>>
    suspend fun createComment(taskId: String, text: String, parentCommentId: UUID? = null): Result<TaskComment>
    suspend fun updateComment(commentId: UUID, text: String): Result<TaskComment>
    suspend fun deleteComment(commentId: UUID): Result<Unit>
    suspend fun searchComments(taskId: String, query: String): Result<List<UUID>>
}