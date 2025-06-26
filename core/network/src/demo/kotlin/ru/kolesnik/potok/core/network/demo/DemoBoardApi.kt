package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.BoardApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoBoardApi @Inject constructor() : BoardApi {
    
    override suspend fun getCardAssignees(isTheme: Boolean?): List<String> {
        // Заглушка для демо-режима
        return listOf("449927", "91112408208", "1796367")
    }
    
    override suspend fun getCardAuthors(isTheme: Boolean?): List<String> {
        // Заглушка для демо-режима
        return listOf("449927", "91112408208", "1796367")
    }
}