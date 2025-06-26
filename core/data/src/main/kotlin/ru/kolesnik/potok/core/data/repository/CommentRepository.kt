package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.result.Result

interface CommentRepository {
    fun getTaskComments(taskId: String, limit: Int = 10, offset: Int = 0): Flow<List<TaskComment>>
    suspend fun createComment(taskId: String, text: String, parentCommentId: String? = null): Result<TaskComment>
    suspend fun getComment(commentId: String): Result<TaskComment>
    suspend fun updateComment(commentId: String, text: String): Result<TaskComment>
    suspend fun deleteComment(commentId: String): Result<Unit>
    suspend fun searchComments(taskId: String, query: String): Result<List<String>>
    suspend fun getCommentCount(taskId: String): Int
}