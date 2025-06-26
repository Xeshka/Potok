package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.model.api.*
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoCommentApi @Inject constructor() : CommentApi {
    
    override suspend fun getTaskComments(taskId: String, limit: Int, offset: Int, sort: String?): TaskCommentPageDTO {
        // Заглушка для демо-режима
        return TaskCommentPageDTO(
            limit = limit,
            offset = offset,
            total = 0,
            items = emptyList()
        )
    }
    
    override suspend fun createComment(taskId: String, request: TaskCommentRq): TaskCommentDTO {
        // Заглушка для демо-режима
        return TaskCommentDTO(
            id = UUID.randomUUID(),
            parentCommentId = request.parentCommentId,
            owner = "449927",
            text = request.text,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun getComment(commentId: UUID): TaskCommentDTO {
        // Заглушка для демо-режима
        return TaskCommentDTO(
            id = commentId,
            owner = "449927",
            text = "Comment text",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO {
        // Заглушка для демо-режима
        return TaskCommentDTO(
            id = commentId,
            parentCommentId = request.parentCommentId,
            owner = "449927",
            text = request.text,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun deleteComment(commentId: UUID) {
        // Заглушка для демо-режима
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // Заглушка для демо-режима
        return SearchRs(
            commentIds = emptyList()
        )
    }
    
    override suspend fun searchCommentsPost(taskId: String, request: SearchQuery): SearchRs {
        // Заглушка для демо-режима
        return SearchRs(
            commentIds = emptyList()
        )
    }
}