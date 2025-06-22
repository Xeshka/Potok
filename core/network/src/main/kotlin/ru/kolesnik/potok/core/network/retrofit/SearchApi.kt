package ru.kolesnik.potok.core.network.retrofit

import retrofit2.http.*
import ru.kolesnik.potok.core.network.model.api.*

interface SearchApi {
    
    @GET("/api/service-task-main/api/v1/search")
    suspend fun search(@Query("query") query: String): SearchResult
}