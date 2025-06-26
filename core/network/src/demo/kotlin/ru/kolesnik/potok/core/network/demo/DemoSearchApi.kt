package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchResult
import ru.kolesnik.potok.core.network.model.api.SearchRs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoSearchApi @Inject constructor() : SearchApi {
    
    override suspend fun search(request: SearchQuery): SearchResult {
        // Заглушка для демо-режима
        return SearchResult(
            tasks = emptyList(),
            comments = emptyList(),
            checks = emptyList()
        )
    }
    
    override suspend fun search(query: String): SearchResult {
        // Заглушка для демо-режима
        return SearchResult(
            tasks = emptyList(),
            comments = emptyList(),
            checks = emptyList()
        )
    }
    
    override suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs {
        // Заглушка для демо-режима
        return SearchRs(
            commentIds = emptyList()
        )
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // Заглушка для демо-режима
        return SearchRs(
            commentIds = emptyList()
        )
    }
}