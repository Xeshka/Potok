package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.BoardApi
import ru.kolesnik.potok.core.network.datasource.BoardDataSource
import javax.inject.Inject

class RetrofitBoardDataSource @Inject constructor(
    private val api: BoardApi
) : BoardDataSource {
    override suspend fun getCardAssignees(isTheme: Boolean?) = api.getCardAssignees(isTheme)
    override suspend fun getCardAuthors(isTheme: Boolean?) = api.getCardAuthors(isTheme)
}