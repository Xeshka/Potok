package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.UUID

interface ChecklistDataSource {
    suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO>
    suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO
    suspend fun updateChecklist(taskId: String, request: ChecklistRq): ChecklistDTO
    suspend fun updateChecklistTask(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO
    suspend fun deleteChecklistTask(checklistTaskId: UUID)
    suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO
    suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO
    suspend fun toggleChecklistTask(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO
    suspend fun moveChecklistTask(request: ChecklistTaskMoveRq)
}