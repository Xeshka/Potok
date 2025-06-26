package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {

    override suspend fun searchTasks(query: String): Result<List<Task>> {
        return try {
            when (val result = searchApi.searchTasks(query)) {
                is Result.Success -> {
                    val tasks = result.data.map { it.toModel() }
                    Result.Success(tasks)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchEmployees(query: String): Result<List<Employee>> {
        return try {
            when (val result = searchApi.searchEmployees(query)) {
                is Result.Success -> {
                    val employees = result.data.map { it.toModel() }
                    Result.Success(employees)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}