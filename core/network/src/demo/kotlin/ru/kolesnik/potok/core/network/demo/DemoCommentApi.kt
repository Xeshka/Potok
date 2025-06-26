package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchRs
import ru.kolesnik.potok.core.network.model.api.TaskCommentDTO
import ru.kolesnik.potok.core.network.model.api.TaskCommentPageDTO
import ru.kolesnik.potok.core.network.model.api.TaskCommentRq
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
        val taskComments = comments[taskId] ?: emptyList()
        
        // Применяем пагинацию
        val paginatedComments = taskComments
            .drop(offset)
            .take(limit)
        
        return TaskCommentPageDTO(
            limit = limit,
            offset = offset,
            total = taskComments.size,
            items = paginatedComments
        )
    }
    
    override suspend fun createComment(taskId: String, request: TaskCommentRq): TaskCommentDTO {
        val commentId = UUID.randomUUID()
        val now = OffsetDateTime.now()
        
        val comment = TaskCommentDTO(
            id = commentId,
            parentCommentId = request.parentCommentId,
            owner = "449927", // Текущий пользователь
            text = request.text,
            createdAt = now,
            updatedAt = now
        )
        
        val taskComments = comments.getOrPut(taskId) { mutableListOf() }
        taskComments.add(comment)
        
        return comment
    }
    
    override suspend fun getComment(commentId: UUID): TaskCommentDTO {
        // Ищем комментарий по ID
        for ((_, taskComments) in comments) {
            val comment = taskComments.find { it.id == commentId }
            if (comment != null) {
                return comment
            }
        }
        
        // Если комментарий не найден, возвращаем заглушечный комментарий
        return TaskCommentDTO(
            id = commentId,
            text = "Комментарий не найден",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO {
        // Ищем комментарий по ID
        for ((_, taskComments) in comments) {
            val index = taskComments.indexOfFirst { it.id == commentId }
            if (index != -1) {
                val updatedComment = taskComments[index].copy(
                    text = request.text,
                    updatedAt = OffsetDateTime.now()
                )
                taskComments[index] = updatedComment
                return updatedComment
            }
        }
        
        // Если комментарий не найден, возвращаем заглушечный комментарий
        return TaskCommentDTO(
            id = commentId,
            text = request.text,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun deleteComment(commentId: UUID) {
        // Удаляем комментарий по ID
        for ((_, taskComments) in comments) {
            val index = taskComments.indexOfFirst { it.id == commentId }
            if (index != -1) {
                taskComments.removeAt(index)
                break
            }
        }
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // Ищем комментарии, содержащие запрос
        val taskComments = comments[taskId] ?: emptyList()
        val matchingComments = taskComments.filter { 
            it.text.contains(query, ignoreCase = true) 
        }
        
        return SearchRs(commentIds = matchingComments.map { it.id })
    }
    
    override suspend fun searchCommentsPost(taskId: String, request: SearchQuery): SearchRs {
        return searchComments(taskId, request.query)
    }
}