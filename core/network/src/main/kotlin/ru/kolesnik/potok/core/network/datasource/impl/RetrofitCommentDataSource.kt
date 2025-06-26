package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.CommentApi
import ru.kolesnik.potok.core.network.datasource.CommentDataSource
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject

class RetrofitCommentDataSource @Inject constructor(
    private val api: CommentApi
) : CommentDataSource {
    override suspend fun getTaskComments(taskId: String, limit: Int, offset: Int, sort: String?) = 
        api.getTaskComments(taskId, limit, offset, sort)
    
    override suspend fun createComment(taskId: String, request: TaskCommentRq) = 
        api.createComment(taskId, request)
    
    override suspend fun getComment(commentId: UUID) = api.getComment(commentId)
    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq) = 
        api.updateComment(commentId, request)
    
    override suspend fun deleteComment(commentId: UUID) = api.deleteComment(commentId)
    override suspend fun searchComments(taskId: String, query: String) = 
        api.searchComments(taskId, query)
    
    override suspend fun searchCommentsPost(taskId: String, request: SearchQuery) = 
        api.searchCommentsPost(taskId, request)
}