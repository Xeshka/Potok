package ru.kolesnik.potok.core.network.retrofit

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.NetworkTask

interface TaskArchiveApi {
    
    @GET("/api/service-task-main/api/v1/task-archive")
    suspend fun getTaskArchive(
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("status") status: String? = null
    ): TaskArchivePageDTO
    
    @POST("/api/service-task-main/api/v1/task-archive/{taskId}")
    suspend fun archiveTask(@Path("taskId") taskId: String)
    
    @DELETE("/api/service-task-main/api/v1/task-archive")
    suspend fun deleteArchivedTasks(@Query("taskIds") taskIds: String)
    
    @POST("/api/service-task-main/api/v1/task-archive/restore")
    suspend fun restoreTasks(@Body request: TaskExternalIds)
    
    @GET("/api/service-task-main/api/v1/task-archive/{taskId}/details")
    suspend fun getArchivedTaskDetails(@Path("taskId") taskId: String): NetworkTask
}