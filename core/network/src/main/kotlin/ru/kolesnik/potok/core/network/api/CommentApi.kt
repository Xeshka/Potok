package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface CommentApi {
    
    @GET("/api/service-task-comment/api/v1/task-comments/task/{id}")
    suspend fun getTaskComments(
        @Path("id") taskId: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("sort") sort: String? = null
    ): TaskCommentPageDTO
    
    @POST("/api/service-task-comment/api/v1/task-comments/{taskId}")
    suspend fun createComment(
        @Path("taskId") taskId: String,
        @Body request: TaskCommentRq
    ): TaskCommentDTO
    
    @GET("/api/service-task-comment/api/v1/task-comments/{id}")
    suspend fun getComment(@Path("id") commentId: UUID): TaskCommentDTO
    
    @PUT("/api/service-task-comment/api/v1/task-comments/{id}")
    suspend fun updateComment(
        @Path("id") commentId: UUID,
        @Body request: TaskCommentRq
    ): TaskCommentDTO
    
    @DELETE("/api/service-task-comment/api/v1/task-comments/{id}")
    suspend fun deleteComment(@Path("id") commentId: UUID)
    
    @GET("/api/service-task-comment/api/v1/task-comments/search/task/{id}")
    suspend fun searchComments(
        @Path("id") taskId: String,
        @Query("query") query: String
    ): SearchRs
    
    @POST("/api/service-task-comment/api/v1/task-comments/search/task/{id}")
    suspend fun searchCommentsPost(
        @Path("id") taskId: String,
        @Body request: SearchQuery
    ): SearchRs
}