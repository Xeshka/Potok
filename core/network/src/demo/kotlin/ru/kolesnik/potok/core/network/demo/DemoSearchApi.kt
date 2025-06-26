package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.retrofit.SearchApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoSearchApi @Inject constructor() : SearchApi {
    
    override suspend fun search(request: SearchQuery): SearchResult {
        // Return empty search results for demo
        return SearchResult(
            tasks = emptyList(),
            comments = emptyList(),
            checks = emptyList()
        )
    }
    
    override suspend fun search(query: String): SearchResult {
        // Return empty search results for demo
        return SearchResult(
            tasks = emptyList(),
            comments = emptyList(),
            checks = emptyList()
        )
    }
    
    override suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs {
        // Return empty comment search results for demo
        return SearchRs(commentIds = emptyList())
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // Return empty comment search results for demo
        return SearchRs(commentIds = emptyList())
    }
}