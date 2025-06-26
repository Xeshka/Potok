package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.datasource.SearchDataSource
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import javax.inject.Inject

class RetrofitSearchDataSource @Inject constructor(
    private val api: SearchApi
) : SearchDataSource {
    override suspend fun search(request: SearchQuery) = api.search(request)
    override suspend fun search(query: String) = api.search(query)
    override suspend fun searchComments(taskId: String, request: SearchQuery) = api.searchComments(taskId, request)
    override suspend fun searchComments(taskId: String, query: String) = api.searchComments(taskId, query)
}