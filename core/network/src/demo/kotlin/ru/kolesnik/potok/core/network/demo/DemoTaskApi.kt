package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.retrofit.TaskApi
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoTaskApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : TaskApi {
    
    override suspend fun createTask(request: TaskRq): TaskRs {
        // Create a mock task response
        return TaskRs(
            id = UUID.randomUUID().toString().substring(0, 8),
            title = request.payload.title ?: "New Task",
            subtitle = request.payload.subtitle,
            mainOrder = 0,
            source = request.payload.source ?: "TRELLO",
            taskOwner = "449927", // Default demo user
            creationDate = OffsetDateTime.now(),
            payload = request.payload,
            internalId = (1000..9999).random().toLong(),
            lifeAreaPlacement = 1,
            flowPlacement = 1,
            assignees = request.payload.assignees?.map { 
                TaskAssigneeRs(employeeId = it, complete = false) 
            },
            commentCount = 0,
            attachmentCount = 0,
            checkList = emptyList(),
            cardId = UUID.randomUUID()
        )
    }
    
    override suspend fun updateTask(taskId: String, request: PatchPayload) {
        // No-op in demo mode
    }
    
    override suspend fun getTaskId(taskId: String): Long {
        // Return a random ID for demo
        return (1000..9999).random().toLong()
    }
    
    override suspend fun checkTaskAllowed(taskId: Long) {
        // No-op in demo mode
    }
    
    override suspend fun getTaskDetails(taskId: String): TaskRs {
        // Find the task in our demo data
        val allAreas = dataSource.getFullLifeAreas()
        for (area in allAreas) {
            area.flows?.forEach { flow ->
                flow.tasks?.find { it.id == taskId }?.let { return it }
            }
        }
        
        // If not found, return a mock
        return TaskRs(
            id = taskId,
            title = "Task not found",
            subtitle = null,
            mainOrder = 0,
            source = "DEMO",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now(),
            payload = TaskPayload(
                title = "Task not found",
                description = "This is a mock task for demo purposes",
                assignees = emptyList()
            ),
            internalId = 0,
            lifeAreaPlacement = 0,
            flowPlacement = 0,
            assignees = emptyList(),
            commentCount = 0,
            attachmentCount = 0,
            cardId = UUID.randomUUID()
        )
    }
    
    override suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq) {
        // No-op in demo mode
    }
    
    override suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq) {
        // No-op in demo mode
    }
    
    override suspend fun returnTask(taskId: String, assignee: String) {
        // No-op in demo mode
    }
    
    override suspend fun getTaskArchive(
        limit: Int?,
        offset: Int?,
        sort: String?,
        status: String?
    ): TaskArchivePageDTO {
        // Return empty archive for demo
        return TaskArchivePageDTO(
            limit = limit ?: 10,
            offset = offset ?: 0,
            total = 0,
            items = emptyList()
        )
    }
    
    override suspend fun deleteTasksFromArchive(taskIds: String) {
        // No-op in demo mode
    }
    
    override suspend fun restoreTasksFromArchive(request: TaskExternalIds) {
        // No-op in demo mode
    }
    
    override suspend fun archiveTask(taskId: String) {
        // No-op in demo mode
    }
    
    override suspend fun getArchivedTaskDetails(taskId: String): TaskRs {
        // Return a mock archived task
        return TaskRs(
            id = taskId,
            title = "Archived Task",
            subtitle = null,
            mainOrder = 0,
            source = "DEMO",
            taskOwner = "449927",
            creationDate = OffsetDateTime.now().minusDays(30),
            payload = TaskPayload(
                title = "Archived Task",
                description = "This is a mock archived task for demo purposes",
                assignees = emptyList()
            ),
            internalId = 0,
            lifeAreaPlacement = 0,
            flowPlacement = 0,
            assignees = emptyList(),
            commentCount = 0,
            attachmentCount = 0,
            cardId = UUID.randomUUID()
        )
    }
}