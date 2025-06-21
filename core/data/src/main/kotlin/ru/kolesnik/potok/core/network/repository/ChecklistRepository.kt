package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.result.Result
import java.time.OffsetDateTime
import java.util.UUID

interface ChecklistRepository {
    suspend fun getChecklist(taskId: String): Result<List<ChecklistTask>>
    suspend fun createChecklistTask(taskId: String, title: String): Result<ChecklistTask>
    suspend fun updateChecklistTask(checklistTaskId: UUID, title: String): Result<ChecklistTask>
    suspend fun deleteChecklistTask(checklistTaskId: UUID): Result<Unit>
    suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, deadline: OffsetDateTime?): Result<ChecklistTask>
    suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, responsibles: List<String>?): Result<ChecklistTask>
    suspend fun toggleChecklistTask(checklistTaskId: UUID, done: Boolean): Result<ChecklistTask>
    suspend fun moveChecklistTask(checklistTaskId: UUID, placement: Int?): Result<Unit>
}