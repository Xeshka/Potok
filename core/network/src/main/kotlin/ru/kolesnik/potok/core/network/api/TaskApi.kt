package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.api.TaskArchivePageDTO
import ru.kolesnik.potok.core.network.model.api.TaskExternalIds
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.api.LifeAreaPositionRq
import ru.kolesnik.potok.core.network.model.potok.PatchPayload

interface TaskApi {
    
    @POST("/api/service-task-main/api/v1/tasks")
    suspend fun createTask(
        @Body request: TaskRq
    ): TaskRs
    
    @PATCH("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: String,
        @Body request: PatchPayload
    )
    
    @GET("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun getTaskId(
        @Path("taskId") taskId: String
    ): Long
    
    @GET("/api/service-task-main/api/v1/tasks/{taskId}/allowed")
    suspend fun checkTaskAllowed(
        @Path("taskId") taskId: Long
    )
    
    @GET("/api/service-task-main/api/v1/tasks/{taskId}/details")
    suspend fun getTaskDetails(
        @Path("taskId") taskId: String
    ): TaskRs
    
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
    suspend fun returnTask(
        @Path("taskId") taskId: String,
        @Path("assignee") assignee: String
    )
    
    @GET("/api/service-task-main/api/v1/task-archive")
    suspend fun getTaskArchive(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("status") status: String? = null
    ): TaskArchivePageDTO
    
    @DELETE("/api/service-task-main/api/v1/task-archive")
    suspend fun deleteTasksFromArchive(
        @Query("taskIds") taskIds: String
    )
    
    @POST("/api/service-task-main/api/v1/task-archive/restore")
    suspend fun restoreTasksFromArchive(
        @Body request: TaskExternalIds
    )
    
    @POST("/api/service-task-main/api/v1/task-archive/{taskId}")
    suspend fun archiveTask(
        @Path("taskId") taskId: String
    )
    
    @GET("/api/service-task-main/api/v1/task-archive/{taskId}/details")
    suspend fun getArchivedTaskDetails(
        @Path("taskId") taskId: String
    ): TaskRs
}