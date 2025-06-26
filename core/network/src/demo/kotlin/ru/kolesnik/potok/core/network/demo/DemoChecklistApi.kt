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
        // Ищем задачу в демо-данных
        val allAreas = dataSource.getFullLifeAreas()
        for (area in allAreas) {
            for (flow in area.flows ?: emptyList()) {
                for (task in flow.tasks ?: emptyList()) {
                    if (task.id == taskId || task.cardId.toString() == taskId) {
                        return task.checkList ?: emptyList()
                    }
                }
            }
        }
        
        return emptyList()
    }
    
    override suspend fun createChecklist(taskId: String, request: ChecklistRq): ChecklistDTO {
        // Создаем заглушку для ответа
        val checklistTasks = request.checklist.mapIndexed { index, item ->
            ChecklistTaskDTO(
                id = UUID.randomUUID(),
                title = item.title,
                done = item.done ?: false,
                placement = index,
                responsibles = item.responsibles,
                deadline = item.deadline
            )
        }
        
        return ChecklistDTO(checklist = checklistTasks)
    }
    
    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun updateChecklistTaskTitle(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        // Возвращаем заглушку с обновленным названием
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = request.title,
            done = false,
            placement = 0
        )
    }
    
    override suspend fun deleteChecklistTask(checklistTaskId: UUID) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO {
        // Возвращаем заглушку с обновленным дедлайном
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача чек-листа",
            done = false,
            placement = 0,
            deadline = request.deadline
        )
    }
    
    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO {
        // Возвращаем заглушку с обновленными ответственными
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача чек-листа",
            done = false,
            placement = 0,
            responsibles = request.responsibles
        )
    }
    
    override suspend fun updateChecklistTaskStatus(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO {
        // Возвращаем заглушку с обновленным статусом
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача чек-листа",
            done = done,
            placement = 0
        )
    }
    
    override suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        // Создаем заглушку для ответа
        return ChecklistTaskDTO(
            id = UUID.randomUUID(),
            title = request.title,
            done = false,
            placement = 0
        )
    }
}