package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.UUID

interface CommentDataSource {
    suspend fun getTaskComments(taskId: String, limit: Int = 10, offset: Int = 0, sort: String? = null): TaskCommentPageDTO
    suspend fun createComment(taskId: String, request: TaskCommentRq): TaskCommentDTO
    suspend fun getComment(commentId: UUID): TaskCommentDTO
    suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO
    suspend fun deleteComment(commentId: UUID)
    suspend fun searchComments(taskId: String, query: String): SearchRs
}