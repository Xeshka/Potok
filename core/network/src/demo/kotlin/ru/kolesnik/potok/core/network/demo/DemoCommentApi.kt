package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.model.api.*
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoCommentApi @Inject constructor() : CommentApi {
    
    // Хранилище комментариев для демо-режима
    private val comments = mutableMapOf<String, MutableList<TaskCommentDTO>>()
    
    override suspend fun getTaskComments(taskId: String, limit: Int, offset: Int, sort: String?): TaskCommentPageDTO {
        val taskComments = comments[taskId] ?: mutableListOf()
        
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
            owner = "449927", // ID текущего пользователя
            text = request.text,
            createdAt = now,
            updatedAt = now
        )
        
        // Добавляем комментарий в хранилище
        if (!comments.containsKey(taskId)) {
            comments[taskId] = mutableListOf()
        }
        comments[taskId]?.add(comment)
        
        return comment
    }
    
    override suspend fun getComment(commentId: UUID): TaskCommentDTO {
        // Ищем комментарий во всем хранилище
        for ((_, taskComments) in comments) {
            val comment = taskComments.find { it.id == commentId }
            if (comment != null) {
                return comment
            }
        }
        
        // Если комментарий не найден, возвращаем заглушку
        return TaskCommentDTO(
            id = commentId,
            text = "Комментарий не найден",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO {
        // Ищем и обновляем комментарий
        for ((_, taskComments) in comments) {
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
        
        // Если комментарий не найден, возвращаем заглушку
        return TaskCommentDTO(
            id = commentId,
            text = request.text,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
    }
    
    override suspend fun deleteComment(commentId: UUID) {
        // Удаляем комментарий из хранилища
        for ((_, taskComments) in comments) {
            taskComments.removeIf { it.id == commentId }
        }
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        val taskComments = comments[taskId] ?: emptyList()
        
        // Ищем комментарии, содержащие запрос
        val matchingCommentIds = taskComments
            .filter { it.text.contains(query, ignoreCase = true) }
            .map { it.id }
        
        return SearchRs(commentIds = matchingCommentIds)
    }
    
    override suspend fun searchCommentsPost(taskId: String, request: SearchQuery): SearchRs {
        return searchComments(taskId, request.query)
    }
}