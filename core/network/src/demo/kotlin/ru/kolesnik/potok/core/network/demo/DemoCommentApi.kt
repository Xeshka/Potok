package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.retrofit.CommentApi
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoCommentApi @Inject constructor() : CommentApi {
    
    private val comments = mutableMapOf<String, MutableList<TaskCommentDTO>>()
    
    override suspend fun getTaskComments(
        taskId: String,
        limit: Int,
        offset: Int,
        sort: String?
    ): TaskCommentPageDTO {
        val taskComments = comments[taskId] ?: mutableListOf()
        
        return TaskCommentPageDTO(
            limit = limit,
            offset = offset,
            total = taskComments.size,
            items = taskComments.drop(offset).take(limit)
        )
    }
    
    override suspend fun createComment(taskId: String, request: TaskCommentRq): TaskCommentDTO {
        val commentId = UUID.randomUUID()
        val now = OffsetDateTime.now()
        
        val comment = TaskCommentDTO(
            id = commentId,
            parentCommentId = request.parentCommentId,
            owner = "449927", // Default demo user
            text = request.text,
            createdAt = now,
            updatedAt = now
        )
        
        if (!comments.containsKey(taskId)) {
            comments[taskId] = mutableListOf()
        }
        
        comments[taskId]?.add(comment)
        
        return comment
    }
    
    override suspend fun getComment(commentId: UUID): TaskCommentDTO {
        // Search for the comment in all tasks
        for (taskComments in comments.values) {
            val comment = taskComments.find { it.id == commentId }
            if (comment != null) {
                return comment
            }
        }
        
        // If not found, return a mock
        return TaskCommentDTO(
            id = commentId,
            parentCommentId = null,
            owner = "449927",
            text = "Comment not found",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO {
        // Search for the comment in all tasks and update it
        for (taskComments in comments.values) {
            val index = taskComments.indexOfFirst { it.id == commentId }
            if (index != -1) {
                val oldComment = taskComments[index]
                val updatedComment = oldComment.copy(
                    text = request.text,
                    updatedAt = OffsetDateTime.now()
                )
                taskComments[index] = updatedComment
                return updatedComment
            }
        }
        
        // If not found, return a mock
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
        // Search for the comment in all tasks and remove it
        for (taskComments in comments.values) {
            taskComments.removeIf { it.id == commentId }
        }
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        val taskComments = comments[taskId] ?: mutableListOf()
        val matchingComments = taskComments.filter { 
            it.text.contains(query, ignoreCase = true) 
        }
        
        return SearchRs(commentIds = matchingComments.map { it.id })
    }
    
    override suspend fun searchCommentsPost(taskId: String, request: SearchQuery): SearchRs {
        return searchComments(taskId, request.query)
    }
}