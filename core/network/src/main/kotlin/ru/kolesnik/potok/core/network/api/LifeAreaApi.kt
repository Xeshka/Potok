package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface LifeAreaApi {
    
    @GET("/api/service-task-main/api/v1/life-areas")
    suspend fun getLifeAreas(): List<LifeAreaDTO>
    
    @POST("/api/service-task-main/api/v1/life-areas")
    suspend fun createLifeArea(
        @Body request: LifeAreaRq
    ): LifeAreaDTO
    
    @POST("/api/service-task-main/api/v1/life-areas/collocate")
    suspend fun collocateLifeAreas(
        @Body lifeAreaIds: List<UUID>
    )
    
    @GET("/api/service-task-main/api/v1/life-areas/full")
    suspend fun getFullLifeAreas(): List<LifeAreaDTO>
    
    @POST("/api/service-task-main/api/v1/life-areas/move")
    suspend fun moveLifeArea(
        @Body request: LifeAreaMoveDTO
    )
    
    @PUT("/api/service-task-main/api/v1/life-areas/{id}")
    suspend fun updateLifeArea(
        @Path("id") id: UUID,
        @Body request: LifeAreaRq
    ): LifeAreaDTO
    
    @DELETE("/api/service-task-main/api/v1/life-areas/{id}")
    suspend fun deleteLifeArea(
        @Path("id") id: UUID
    )
}