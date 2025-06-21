package ru.kolesnik.potok.core.network.retrofit

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import java.util.UUID

interface LifeAreaApi {
    
    @GET("/api/service-task-main/api/v1/life-areas")
    suspend fun getLifeAreas(): List<NetworkLifeArea>
    
    @POST("/api/service-task-main/api/v1/life-areas")
    suspend fun createLifeArea(@Body request: LifeAreaRq): NetworkLifeArea
    
    @PUT("/api/service-task-main/api/v1/life-areas/{id}")
    suspend fun updateLifeArea(
        @Path("id") id: UUID,
        @Body request: LifeAreaRq
    ): NetworkLifeArea
    
    @DELETE("/api/service-task-main/api/v1/life-areas/{id}")
    suspend fun deleteLifeArea(@Path("id") id: UUID)
    
    @POST("/api/service-task-main/api/v1/life-areas/move")
    suspend fun moveLifeArea(@Body request: LifeAreaMoveDTO)
    
    @POST("/api/service-task-main/api/v1/life-areas/collocate")
    suspend fun collocateLifeAreas(@Body lifeAreaIds: List<UUID>)
}