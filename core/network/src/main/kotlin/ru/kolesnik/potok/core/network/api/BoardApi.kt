package ru.kolesnik.potok.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface BoardApi {
    
    @GET("/api/service-task-main/api/v1/boards/card-assignees")
    suspend fun getCardAssignees(
        @Query("isTheme") isTheme: Boolean? = null
    ): List<String>
    
    @GET("/api/service-task-main/api/v1/boards/card-authors")
    suspend fun getCardAuthors(
        @Query("isTheme") isTheme: Boolean? = null
    ): List<String>
}