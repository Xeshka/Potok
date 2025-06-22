package ru.kolesnik.potok.core.network.api

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchResult
import ru.kolesnik.potok.core.network.model.api.SearchRs

interface SearchApi {
    
    @POST("/api/service-task-main/api/v1/search")
    suspend fun search(
        @Body request: SearchQuery
    ): SearchResult
    
    @GET("/api/service-task-main/api/v1/search")
    suspend fun search(
        @Query("query") query: String
    ): SearchResult
    
    @POST("/api/service-task-comment/api/v1/task-comments/search/task/{id}")
    suspend fun searchComments(
        @Path("id") taskId: String,
        @Body request: SearchQuery
    ): SearchRs
    
    @GET("/api/service-task-comment/api/v1/task-comments/search/task/{id}")
    suspend fun searchComments(
        @Path("id") taskId: String,
        @Query("query") query: String
    ): SearchRs
}