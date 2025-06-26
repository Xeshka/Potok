package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.BoardApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoBoardApi @Inject constructor() : BoardApi {
    
    override suspend fun getCardAssignees(isTheme: Boolean?): List<String> {
        // Возвращаем список демо-исполнителей
        return listOf(
            "449927",  // Колесник Никита
            "91112408208", // Кайнар Андрей
            "1796367",  // Иванов Иван
            "21082254", // Петров Петр
            "2004437"   // Сидоров Сидор
        )
    }
    
    override suspend fun getCardAuthors(isTheme: Boolean?): List<String> {
        // Возвращаем список демо-авторов
        return listOf(
            "449927",  // Колесник Никита
            "91112408208", // Кайнар Андрей
            "1796367",  // Иванов Иван
            "21082254", // Петров Петр
            "2004437"   // Сидоров Сидор
        )
    }
}