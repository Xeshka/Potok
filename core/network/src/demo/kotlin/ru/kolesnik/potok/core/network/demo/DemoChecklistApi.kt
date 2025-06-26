package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.model.api.ChecklistDTO
import ru.kolesnik.potok.core.network.model.api.ChecklistRq
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskDTO
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskDeadlineRq
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskMoveRq
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskResponsiblesRq
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskTitleRq
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoChecklistApi @Inject constructor() : ChecklistApi {
    
    private val checklistItems = mutableMapOf<String, MutableList<ChecklistTaskDTO>>()
    
    override suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO> {
        return checklistItems[taskId] ?: emptyList()
    }
    
    override suspend fun createChecklist(taskId: String, request: ChecklistRq): ChecklistDTO {
        val items = request.checklist.mapIndexed { index, item ->
            ChecklistTaskDTO(
                id = UUID.randomUUID(),
                title = item.title,
                done = item.done ?: false,
                placement = index + 1,
                responsibles = item.responsibles,
                deadline = item.deadline
            )
        }
        
        checklistItems[taskId] = items.toMutableList()
        
        return ChecklistDTO(checklist = items)
    }
    
    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) {
        // Пустая реализация для демо
    }
    
    override suspend fun updateChecklistTaskTitle(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        // Ищем задачу в чек-листах
        for ((taskId, items) in checklistItems) {
            val index = items.indexOfFirst { it.id == checklistTaskId }
            if (index != -1) {
                val updatedItem = items[index].copy(title = request.title)
                items[index] = updatedItem
                return updatedItem
            }
        }
        
        // Если задача не найдена, возвращаем заглушечную задачу
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = request.title,
            done = false,
            placement = 1
        )
    }
    
    override suspend fun deleteChecklistTask(checklistTaskId: UUID) {
        // Удаляем задачу из чек-листов
        for ((taskId, items) in checklistItems) {
            val index = items.indexOfFirst { it.id == checklistTaskId }
            if (index != -1) {
                items.removeAt(index)
                break
            }
        }
    }
    
    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO {
        // Ищем задачу в чек-листах
        for ((taskId, items) in checklistItems) {
            val index = items.indexOfFirst { it.id == checklistTaskId }
            if (index != -1) {
                val updatedItem = items[index].copy(deadline = request.deadline)
                items[index] = updatedItem
                return updatedItem
            }
        }
        
        // Если задача не найдена, возвращаем заглушечную задачу
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача не найдена",
            done = false,
            placement = 1,
            deadline = request.deadline
        )
    }
    
    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO {
        // Ищем задачу в чек-листах
        for ((taskId, items) in checklistItems) {
            val index = items.indexOfFirst { it.id == checklistTaskId }
            if (index != -1) {
                val updatedItem = items[index].copy(responsibles = request.responsibles)
                items[index] = updatedItem
                return updatedItem
            }
        }
        
        // Если задача не найдена, возвращаем заглушечную задачу
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача не найдена",
            done = false,
            placement = 1,
            responsibles = request.responsibles
        )
    }
    
    override suspend fun updateChecklistTaskStatus(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO {
        // Ищем задачу в чек-листах
        for ((taskId, items) in checklistItems) {
            val index = items.indexOfFirst { it.id == checklistTaskId }
            if (index != -1) {
                val updatedItem = items[index].copy(done = done)
                items[index] = updatedItem
                return updatedItem
            }
        }
        
        // Если задача не найдена, возвращаем заглушечную задачу
        return ChecklistTaskDTO(
            id = checklistTaskId,
            title = "Задача не найдена",
            done = done,
            placement = 1
        )
    }
    
    override suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        val items = checklistItems.getOrPut(taskId) { mutableListOf() }
        
        val newItem = ChecklistTaskDTO(
            id = UUID.randomUUID(),
            title = request.title,
            done = false,
            placement = items.size + 1
        )
        
        items.add(newItem)
        
        return newItem
    }
}