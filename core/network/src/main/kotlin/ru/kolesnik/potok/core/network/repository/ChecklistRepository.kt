package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.model.ChecklistTask

interface ChecklistRepository {
    suspend fun getChecklistForTask(taskId: String): List<ChecklistTask>
    suspend fun createChecklistItem(taskId: String, item: ChecklistTask): ChecklistTask
    suspend fun updateChecklistItem(item: ChecklistTask): ChecklistTask
    suspend fun deleteChecklistItem(itemId: String)
}