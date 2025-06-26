package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.model.api.SearchQuery
import ru.kolesnik.potok.core.network.model.api.SearchResult
import ru.kolesnik.potok.core.network.model.api.SearchRs
import ru.kolesnik.potok.core.network.model.api.TaskId
import ru.kolesnik.potok.core.network.model.api.TaskSearchResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoSearchApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : SearchApi {
    
    override suspend fun search(request: SearchQuery): SearchResult {
        return searchInternal(request.query)
    }
    
    override suspend fun search(query: String): SearchResult {
        return searchInternal(query)
    }
    
    private suspend fun searchInternal(query: String): SearchResult {
        // Получаем все сферы жизни
        val areas = dataSource.gtFullNew()
        
        // Ищем задачи, содержащие запрос в заголовке или описании
        val matchingTasks = mutableListOf<TaskSearchResult>()
        
        for (area in areas) {
            area.flows?.forEach { flow ->
                flow.tasks?.forEach { task ->
                    if (task.title.contains(query, ignoreCase = true) || 
                        task.payload.description?.contains(query, ignoreCase = true) == true) {
                        matchingTasks.add(
                            TaskSearchResult(
                                taskId = TaskId(
                                    id = task.internalId,
                                    externalId = task.id
                                ),
                                title = task.title,
                                description = task.payload.description
                            )
                        )
                    }
                }
            }
        }
        
        // В демо-режиме возвращаем только найденные задачи
        return SearchResult(
            tasks = matchingTasks,
            comments = emptyList(),
            checks = emptyList()
        )
    }
    
    override suspend fun searchComments(taskId: String, request: SearchQuery): SearchRs {
        // В демо-режиме возвращаем пустой результат
        return SearchRs(commentIds = emptyList())
    }
    
    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        // В демо-режиме возвращаем пустой результат
        return SearchRs(commentIds = emptyList())
    }
}