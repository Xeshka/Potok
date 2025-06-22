package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchResult
import ru.kolesnik.potok.core.network.model.api.SearchRs
import javax.inject.Inject

interface SearchRepository {
    suspend fun search(request: SearchQuery): SearchResult
    suspend fun search(query: String): SearchResult
    suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs
    suspend fun searchComments(taskId: String, query: String): SearchRs
}

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi
) : SearchRepository {

    override suspend fun search(request: SearchQuery): SearchResult {
        return api.search(request)
    }

    override suspend fun search(query: String): SearchResult {
        return api.search(query)
    }

    override suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs {
        return api.searchComments(taskId, request)
    }

    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        return api.searchComments(taskId, query)
    }
}