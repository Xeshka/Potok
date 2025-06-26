package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoTaskApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : TaskApi {
    
    override suspend fun createTask(request: TaskRq): TaskRs {
        // Заглушка для демо-режима
        val task = dataSource.createTask(
            ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask(
                lifeAreaId = request.lifeAreaId,
                flowId = request.flowId,
                payload = request.payload
            )
        )
        
        return TaskRs(
            id = task.id,
            title = task.title,
            subtitle = task.subtitle,
            mainOrder = task.mainOrder,
            source = task.source,
            taskOwner = task.taskOwner,
            creationDate = task.creationDate,
            payload = task.payload,
            internalId = task.internalId,
            lifeAreaPlacement = task.lifeAreaPlacement,
            flowPlacement = task.flowPlacement,
            assignees = task.assignees?.map { 
                TaskAssigneeRs(
                    employeeId = it.employeeId,
                    complete = it.complete
                )
            },
            commentCount = task.commentCount,
            attachmentCount = task.attachmentCount,
            checkList = task.checkList?.map {
                ChecklistTaskDTO(
                    id = it.id,
                    title = it.title,
                    done = it.done,
                    placement = it.placement,
                    responsibles = it.responsibles,
                    deadline = it.deadline
                )
            },
            cardId = task.cardId
        )
    }
    
    override suspend fun updateTask(taskId: String, request: PatchPayload) {
        // Заглушка для демо-режима
        dataSource.patchTask(taskId, request)
    }
    
    override suspend fun getTaskId(taskId: String): Long {
        // Заглушка для демо-режима
        return 1L
    }
    
    override suspend fun checkTaskAllowed(taskId: Long) {
        // Заглушка для демо-режима
    }
    
    override suspend fun getTaskDetails(taskId: String): TaskRs {
        // Заглушка для демо-режима
        val task = dataSource.getFull()
            .flatMap { it.flows ?: emptyList() }
            .flatMap { it.tasks ?: emptyList() }
            .find { it.id == taskId }
            ?: throw IllegalStateException("Task not found")
        
        return TaskRs(
            id = task.id,
            title = task.title,
            subtitle = task.subtitle,
            mainOrder = task.mainOrder,
            source = task.source,
            taskOwner = task.taskOwner,
            creationDate = task.creationDate,
            payload = task.payload,
            internalId = task.internalId,
            lifeAreaPlacement = task.lifeAreaPlacement,
            flowPlacement = task.flowPlacement,
            assignees = task.assignees?.map { 
                TaskAssigneeRs(
                    employeeId = it.employeeId,
                    complete = it.complete
                )
            },
            commentCount = task.commentCount,
            attachmentCount = task.attachmentCount,
            checkList = task.checkList?.map {
                ChecklistTaskDTO(
                    id = it.id,
                    title = it.title,
                    done = it.done,
                    placement = it.placement,
                    responsibles = it.responsibles,
                    deadline = it.deadline
                )
            },
            cardId = task.cardId
        )
    }
    
    override suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq) {
        // Заглушка для демо-режима
    }
    
    override suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq) {
        // Заглушка для демо-режима
    }
    
    override suspend fun returnTask(taskId: String, assignee: String) {
        // Заглушка для демо-режима
    }
    
    override suspend fun getTaskArchive(limit: Int?, offset: Int?, sort: String?, status: String?): TaskArchivePageDTO {
        // Заглушка для демо-режима
        return TaskArchivePageDTO(
            limit = limit ?: 10,
            offset = offset ?: 0,
            total = 0,
            items = emptyList()
        )
    }
    
    override suspend fun deleteTasksFromArchive(taskIds: String) {
        // Заглушка для демо-режима
    }
    
    override suspend fun restoreTasksFromArchive(request: TaskExternalIds) {
        // Заглушка для демо-режима
    }
    
    override suspend fun archiveTask(taskId: String) {
        // Заглушка для демо-режима
    }
    
    override suspend fun getArchivedTaskDetails(taskId: String): TaskRs {
        // Заглушка для демо-режима
        return getTaskDetails(taskId)
    }
}