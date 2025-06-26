package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.result.Result

interface ChecklistRepository {
    fun getChecklistTasks(taskId: String): Flow<List<ChecklistTask>>
    suspend fun createChecklistTask(taskId: String, title: String): Result<ChecklistTask>
    suspend fun updateChecklistTask(id: String, title: String?, isCompleted: Boolean?): Result<ChecklistTask>
    suspend fun deleteChecklistTask(id: String): Result<Unit>
    suspend fun toggleChecklistTask(id: String): Result<ChecklistTask>
}