package ru.kolesnik.potok.core.network.datasource

interface BoardDataSource {
    suspend fun getCardAssignees(isTheme: Boolean? = null): List<String>
    suspend fun getCardAuthors(isTheme: Boolean? = null): List<String>
}