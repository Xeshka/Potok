package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.result.Result

interface SearchRepository {
    suspend fun searchTasks(query: String): Result<List<Task>>
    suspend fun searchEmployees(query: String): Result<List<Employee>>
}