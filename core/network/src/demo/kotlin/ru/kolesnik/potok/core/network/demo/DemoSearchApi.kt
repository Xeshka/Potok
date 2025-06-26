package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoSearchApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : SearchApi {
    
    override suspend fun search(request: SearchQuery): SearchResult {
        return performSearch(request.query)
    }
    
    override suspend fun search(query: String): SearchResult {
        return performSearch(query)
    }
    
    override suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs {
        // Возвращаем заглушку с пустым списком результатов
        return SearchRs(commentIds = emptyList())
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // Возвращаем заглушку с пустым списком результатов
        return SearchRs(commentIds = emptyList())
    }
    
    private fun performSearch(query: String): SearchResult {
        // Получаем все данные
        val allAreas = dataSource.getFullLifeAreas()
        
        // Ищем задачи, содержащие запрос в заголовке или описании
        val taskResults = mutableListOf<TaskSearchResult>()
        val commentResults = mutableListOf<CommentSearchResult>()
        val checkResults = mutableListOf<CheckSearchResult>()
        
        for (area in allAreas) {
            for (flow in area.flows ?: emptyList()) {
                for (task in flow.tasks ?: emptyList()) {
                    // Поиск в задачах
                    if (task.title.contains(query, ignoreCase = true) || 
                        task.payload.description?.contains(query, ignoreCase = true) == true) {
                        taskResults.add(
                            TaskSearchResult(
                                taskId = TaskId(task.internalId, task.id),
                                title = task.title,
                                description = task.payload.description
                            )
                        )
                    }
                    
                    // Поиск в чек-листах
                    task.checkList?.forEach { checkItem ->
                        if (checkItem.title.contains(query, ignoreCase = true)) {
                            checkResults.add(
                                CheckSearchResult(
                                    taskId = TaskId(task.internalId, task.id),
                                    taskTitle = task.title,
                                    checkTaskId = checkItem.id,
                                    title = checkItem.title
                                )
                            )
                        }
                    }
                }
            }
        }
        
        return SearchResult(
            tasks = taskResults,
            comments = commentResults,
            checks = checkResults
        )
    }
}