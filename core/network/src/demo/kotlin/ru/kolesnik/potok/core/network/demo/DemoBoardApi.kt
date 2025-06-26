package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.retrofit.BoardApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoBoardApi @Inject constructor() : BoardApi {
    
    override suspend fun getCardAssignees(isTheme: Boolean?): List<String> {
        // Return a list of demo assignees
        return listOf(
            "449927",
            "91112408208",
            "1796367",
            "21082254",
            "2004437"
        )
    }
    
    override suspend fun getCardAuthors(isTheme: Boolean?): List<String> {
        // Return a list of demo authors
        return listOf(
            "449927",
            "91112408208",
            "1796367",
            "21082254",
            "2004437"
        )
    }
}