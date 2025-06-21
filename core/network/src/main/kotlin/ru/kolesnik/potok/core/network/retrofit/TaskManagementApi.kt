package ru.kolesnik.potok.core.network.retrofit

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*

interface TaskManagementApi {
    
    @GET("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun getTaskInternalId(@Path("taskId") taskId: String): Long
    
    @GET("/api/service-task-main/api/v1/tasks/{taskId}/allowed")
    suspend fun checkTaskAccess(@Path("taskId") taskId: Long)
    
    @POST("/api/service-task-main/api/v1/tasks/{taskId}/move-flow")
    suspend fun moveTaskToFlow(
        @Path("taskId") taskId: String,
        @Body request: FlowPositionRq
    )
    
    @POST("/api/service-task-main/api/v1/tasks/{taskId}/move-life-area")
    suspend fun moveTaskToLifeArea(
        @Path("taskId") taskId: String,
        @Body request: LifeAreaPositionRq
    )
    
    @POST("/api/service-task-main/api/v1/tasks/{taskId}/return/{assignee}")
    suspend fun returnTaskToAssignee(
        @Path("taskId") taskId: String,
        @Path("assignee") assignee: String
    )
    
    @GET("/api/service-task-main/api/v1/boards/card-assignees")
    suspend fun getCardAssignees(@Query("isTheme") isTheme: Boolean? = null): List<String>
    
    @GET("/api/service-task-main/api/v1/boards/card-authors")
    suspend fun getCardAuthors(@Query("isTheme") isTheme: Boolean? = null): List<String>
}