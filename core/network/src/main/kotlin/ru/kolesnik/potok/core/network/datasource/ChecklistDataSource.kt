package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface ChecklistDataSource {
    suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO>
    suspend fun createChecklist(taskId: String, request: ChecklistRq): ChecklistDTO
    suspend fun moveChecklistTask(request: ChecklistTaskMoveRq)
    suspend fun updateChecklistTaskTitle(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO
    suspend fun deleteChecklistTask(checklistTaskId: UUID)
    suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO
    suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO
    suspend fun updateChecklistTaskStatus(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO
    suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO
}