package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi
) : SearchRepository {

    override suspend fun search(query: String): List<String> {
        return try {
            val result = api.search(query)
            result.tasks.map { it.title }
        } catch (e: Exception) {
            emptyList()
        }
    }
}