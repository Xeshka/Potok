package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.FlowPositionRq
import ru.kolesnik.potok.core.network.model.api.LifeAreaPositionRq
import ru.kolesnik.potok.core.network.model.api.TaskArchivePageDTO
import ru.kolesnik.potok.core.network.model.api.TaskExternalIds
import ru.kolesnik.potok.core.network.model.api.TaskRq
import ru.kolesnik.potok.core.network.model.api.TaskRs
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
        // Создаем заглушечную задачу
        val taskId = UUID.randomUUID().toString().substring(0, 8)
        val cardId = UUID.randomUUID()
        
        return TaskRs(
            id = taskId,
            title = request.payload.title ?: "Новая задача",
            subtitle = request.payload.subtitle,
            mainOrder = null,
            source = request.payload.source,
            taskOwner = "449927", // Текущий пользователь
            creationDate = OffsetDateTime.now(),
            payload = request.payload,
            internalId = System.currentTimeMillis(),
            lifeAreaPlacement = 1,
            flowPlacement = 1,
            assignees = request.payload.assignees?.map { 
                ru.kolesnik.potok.core.network.model.api.TaskAssigneeRs(
                    employeeId = it,
                    complete = false
                )
            },
            commentCount = 0,
            attachmentCount = 0,
            cardId = cardId
        )
    }
    
    override suspend fun updateTask(taskId: String, request: PatchPayload) {
        // Пустая реализация для демо
    }
    
    override suspend fun getTaskId(taskId: String): Long {
        // Возвращаем заглушечный ID
        return 12345L
    }
    
    override suspend fun checkTaskAllowed(taskId: Long) {
        // Пустая реализация для демо
    }
    
    override suspend fun getTaskDetails(taskId: String): TaskRs {
        // Получаем все сферы жизни
        val areas = dataSource.gtFullNew()
        
        // Ищем задачу по ID во всех сферах и потоках
        for (area in areas) {
            area.flows?.forEach { flow ->
                flow.tasks?.find { it.id == taskId || it.cardId.toString() == taskId }?.let {
                    return it
                }
            }
        }
        
        // Если задача не найдена, возвращаем заглушечную задачу
        return TaskRs(
            id = taskId,
            title = "Задача не найдена",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now(),
            payload = ru.kolesnik.potok.core.network.model.api.TaskPayload(
                title = "Задача не найдена",
                assignees = emptyList()
            ),
            cardId = UUID.randomUUID()
        )
    }
    
    override suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq) {
        // Пустая реализация для демо
    }
    
    override suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq) {
        // Пустая реализация для демо
    }
    
    override suspend fun returnTask(taskId: String, assignee: String) {
        // Пустая реализация для демо
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
        // Пустая реализация для демо
    }
    
    override suspend fun restoreTasksFromArchive(request: TaskExternalIds) {
        // Пустая реализация для демо
    }
    
    override suspend fun archiveTask(taskId: String) {
        // Пустая реализация для демо
    }
    
    override suspend fun getArchivedTaskDetails(taskId: String): TaskRs {
        // Возвращаем заглушечную задачу
        return TaskRs(
            id = taskId,
            title = "Архивная задача",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now(),
            payload = ru.kolesnik.potok.core.network.model.api.TaskPayload(
                title = "Архивная задача",
                assignees = emptyList()
            ),
            cardId = UUID.randomUUID()
        )
    }
}