package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchResult
import ru.kolesnik.potok.core.network.model.api.SearchRs

interface SearchDataSource {
    suspend fun search(request: SearchQuery): SearchResult
    suspend fun search(query: String): SearchResult
    suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs
    suspend fun searchComments(taskId: String, query: String): SearchRs
}