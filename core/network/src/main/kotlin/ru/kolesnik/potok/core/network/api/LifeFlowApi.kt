package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface LifeFlowApi {
    
    @POST("/api/service-task-main/api/v1/life-flows/move")
    suspend fun moveLifeFlow(
        @Body request: LifeFlowMoveDTO
    )
    
    @GET("/api/service-task-main/api/v1/life-flows/{lifeAreaId}")
    suspend fun getLifeFlows(
        @Path("lifeAreaId") lifeAreaId: UUID
    ): List<LifeFlowDTO>
    
    @POST("/api/service-task-main/api/v1/life-flows/{lifeAreaId}")
    suspend fun createLifeFlow(
        @Path("lifeAreaId") lifeAreaId: UUID,
        @Body request: LifeFlowRq
    ): LifeFlowDTO
    
    @POST("/api/service-task-main/api/v1/life-flows/{lifeAreaId}/collocate")
    suspend fun collocateLifeFlows(
        @Path("lifeAreaId") lifeAreaId: UUID,
        @Body flowIds: List<UUID>
    )
    
    @PUT("/api/service-task-main/api/v1/life-flows/{id}")
    suspend fun updateLifeFlow(
        @Path("id") id: UUID,
        @Body request: LifeFlowRq
    ): LifeFlowDTO
    
    @DELETE("/api/service-task-main/api/v1/life-flows/{id}")
    suspend fun deleteLifeFlow(
        @Path("id") id: UUID
    )
}