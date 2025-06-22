package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface ChecklistApi {
    
    @GET("/api/service-task-checklist/api/v1/task-checklist/{taskId}")
    suspend fun getChecklist(
        @Path("taskId") taskId: String
    ): List<ChecklistTaskDTO>
    
    @POST("/api/service-task-checklist/api/v1/task-checklist/list/{taskId}")
    suspend fun createChecklist(
        @Path("taskId") taskId: String,
        @Body request: ChecklistRq
    ): ChecklistDTO
    
    @POST("/api/service-task-checklist/api/v1/task-checklist/move")
    suspend fun moveChecklistTask(
        @Body request: ChecklistTaskMoveRq
    )
    
    @PATCH("/api/service-task-checklist/api/v1/task-checklist/{checklistTaskId}")
    suspend fun updateChecklistTaskTitle(
        @Path("checklistTaskId") checklistTaskId: UUID,
        @Body request: ChecklistTaskTitleRq
    ): ChecklistTaskDTO
    
    @DELETE("/api/service-task-checklist/api/v1/task-checklist/{checklistTaskId}")
    suspend fun deleteChecklistTask(
        @Path("checklistTaskId") checklistTaskId: UUID
    )
    
    @PATCH("/api/service-task-checklist/api/v1/task-checklist/{checklistTaskId}/deadline")
    suspend fun updateChecklistTaskDeadline(
        @Path("checklistTaskId") checklistTaskId: UUID,
        @Body request: ChecklistTaskDeadlineRq
    ): ChecklistTaskDTO
    
    @PATCH("/api/service-task-checklist/api/v1/task-checklist/{checklistTaskId}/responsibles")
    suspend fun updateChecklistTaskResponsibles(
        @Path("checklistTaskId") checklistTaskId: UUID,
        @Body request: ChecklistTaskResponsiblesRq
    ): ChecklistTaskDTO
    
    @POST("/api/service-task-checklist/api/v1/task-checklist/{checklistTaskId}/{done}")
    suspend fun updateChecklistTaskStatus(
        @Path("checklistTaskId") checklistTaskId: UUID,
        @Path("done") done: Boolean
    ): ChecklistTaskDTO
    
    @POST("/api/service-task-checklist/api/v1/task-checklist/{taskId}")
    suspend fun createChecklistTask(
        @Path("taskId") taskId: String,
        @Body request: ChecklistTaskTitleRq
    ): ChecklistTaskDTO
}