package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoTaskApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : TaskApi {
    
    override suspend fun createTask(request: TaskRq): TaskRs {
        // Создаем заглушку для ответа
        val taskId = UUID.randomUUID().toString().substring(0, 8)
        return TaskRs(
            id = taskId,
            title = request.payload.title ?: "Новая задача",
            subtitle = request.payload.subtitle,
            mainOrder = 0,
            source = request.payload.source ?: "TRELLO",
            taskOwner = "449927", // ID текущего пользователя
            creationDate = OffsetDateTime.now(),
            payload = request.payload,
            internalId = System.currentTimeMillis().toInt().toLong(),
            lifeAreaPlacement = 0,
            flowPlacement = 0,
            assignees = request.payload.assignees?.map { 
                TaskAssigneeRs(employeeId = it, complete = false) 
            } ?: emptyList(),
            commentCount = 0,
            attachmentCount = 0,
            cardId = UUID.randomUUID()
        )
    }
    
    override suspend fun updateTask(taskId: String, request: PatchPayload) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun getTaskId(taskId: String): Long {
        // Возвращаем случайный ID
        return System.currentTimeMillis()
    }
    
    override suspend fun checkTaskAllowed(taskId: Long) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun getTaskDetails(taskId: String): TaskRs {
        // Ищем задачу в демо-данных
        val allAreas = dataSource.getFullLifeAreas()
        for (area in allAreas) {
            for (flow in area.flows ?: emptyList()) {
                for (task in flow.tasks ?: emptyList()) {
                    if (task.id == taskId || task.cardId.toString() == taskId) {
                        return task
                    }
                }
            }
        }
        
        // Если задача не найдена, возвращаем заглушку
        return TaskRs(
            id = taskId,
            title = "Задача не найдена",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now(),
            payload = TaskPayload(title = "Задача не найдена"),
            cardId = UUID.randomUUID()
        )
    }
    
    override suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun returnTask(taskId: String, assignee: String) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun getTaskArchive(limit: Int?, offset: Int?, sort: String?, status: String?): TaskArchivePageDTO {
        // Возвращаем пустой список архивных задач
        return TaskArchivePageDTO(
            limit = limit ?: 10,
            offset = offset ?: 0,
            total = 0,
            items = emptyList()
        )
    }
    
    override suspend fun deleteTasksFromArchive(taskIds: String) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun restoreTasksFromArchive(request: TaskExternalIds) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun archiveTask(taskId: String) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun getArchivedTaskDetails(taskId: String): TaskRs {
        // Возвращаем заглушку
        return TaskRs(
            id = taskId,
            title = "Архивная задача",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now(),
            payload = TaskPayload(title = "Архивная задача"),
            cardId = UUID.randomUUID()
        )
    }
}