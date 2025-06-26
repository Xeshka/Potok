package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitSearchDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchApi: RetrofitSearchDataSource
) : SearchRepository {

    override suspend fun searchTasks(query: String): Result<List<Task>> {
        return try {
            val result = searchApi.search(query)
            val tasks = result.tasks.map { taskResult ->
                // Преобразуем результат поиска в Task
                // Здесь нужна дополнительная логика для получения полных данных задачи
                Task(
                    id = taskResult.taskId.externalId,
                    title = taskResult.title,
                    taskOwner = "", // Нужно получить из другого источника
                    payload = ru.kolesnik.potok.core.model.TaskPayload(
                        description = taskResult.description
                    )
                )
            }
            Result.Success(tasks)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchEmployees(query: String): Result<List<Employee>> {
        return try {
            // Поиск сотрудников не реализован в API
            // Возвращаем пустой список
            Result.Success(emptyList())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}