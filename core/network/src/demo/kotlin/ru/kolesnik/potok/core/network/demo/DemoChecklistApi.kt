package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoChecklistApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : ChecklistApi {
    
    override suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO> {
        // Заглушка для демо-режима
        return dataSource.getFull()
            .flatMap { it.flows ?: emptyList() }
            .flatMap { it.tasks ?: emptyList() }
            .find { it.id == taskId }
            ?.checkList?.map {
                ChecklistTaskDTO(
                    id = it.id,
                    title = it.title,
                    done = it.done,
                    placement = it.placement,
                    responsibles = it.responsibles,
                    deadline = it.deadline
                )
            } ?: emptyList()
    }
    
    override suspend fun createChecklist(taskId: String, request: ChecklistRq): ChecklistDTO {
        // Заглушка для демо-режима
        return ChecklistDTO(
            checklist = request.checklist.mapIndexed { index, item ->
                ChecklistTaskDTO(
                    id = UUID.randomUUID(),
                    title = item.title,
                    done = item.done,
                    placement = index,
                    responsibles = item.responsibles,
                    deadline = item.deadline
                )
            }
        )
    }
    
    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) {
        // Заглушка для демо-режима
    }
    
    override suspend fun updateChecklistTaskTitle(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        // Заглушка для демо-режима
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = request.title,
            done = false,
            placement = 0
        )
    }
    
    override suspend fun deleteChecklistTask(checklistTaskId: UUID) {
        // Заглушка для демо-режима
    }
    
    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO {
        // Заглушка для демо-режима
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Task",
            done = false,
            placement = 0,
            deadline = request.deadline
        )
    }
    
    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO {
        // Заглушка для демо-режима
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Task",
            done = false,
            placement = 0,
            responsibles = request.responsibles
        )
    }
    
    override suspend fun updateChecklistTaskStatus(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO {
        // Заглушка для демо-режима
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Task",
            done = done,
            placement = 0
        )
    }
    
    override suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        // Заглушка для демо-режима
        return ChecklistTaskDTO(
            id = UUID.randomUUID(),
            title = request.title,
            done = false,
            placement = 0
        )
    }
}