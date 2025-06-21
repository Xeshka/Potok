package ru.kolesnik.potok.core.network.retrofit

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeFlow
import java.util.UUID

interface LifeFlowApi {
    
    @GET("/api/service-task-main/api/v1/life-flows/{lifeAreaId}")
    suspend fun getLifeFlows(@Path("lifeAreaId") lifeAreaId: UUID): List<NetworkLifeFlow>
    
    @POST("/api/service-task-main/api/v1/life-flows/{lifeAreaId}")
    suspend fun createLifeFlow(
        @Path("lifeAreaId") lifeAreaId: UUID,
        @Body request: LifeFlowRq
    ): NetworkLifeFlow
    
    @PUT("/api/service-task-main/api/v1/life-flows/{id}")
    suspend fun updateLifeFlow(
        @Path("id") id: UUID,
        @Body request: LifeFlowRq
    ): NetworkLifeFlow
    
    @DELETE("/api/service-task-main/api/v1/life-flows/{id}")
    suspend fun deleteLifeFlow(@Path("id") id: UUID)
    
    @POST("/api/service-task-main/api/v1/life-flows/move")
    suspend fun moveLifeFlow(@Body request: LifeFlowMoveDTO)
    
    @POST("/api/service-task-main/api/v1/life-flows/{lifeAreaId}/collocate")
    suspend fun collocateLifeFlows(
        @Path("lifeAreaId") lifeAreaId: UUID,
        @Body flowIds: List<UUID>
    )
}