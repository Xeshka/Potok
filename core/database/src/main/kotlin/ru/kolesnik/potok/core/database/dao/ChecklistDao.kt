package ru.kolesnik.potok.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.model.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.model.ChecklistTaskResponsibleEntity

// ChecklistDao.kt
@Dao
interface ChecklistDao {
    // ChecklistTaskEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateChecklistTask(task: ChecklistTaskEntity)

    @Delete
    suspend fun deleteChecklistTask(task: ChecklistTaskEntity)

    @Query("SELECT * FROM checklist_tasks WHERE task_id = :taskId ORDER BY placement ASC")
    fun getChecklistForTask(taskId: String): Flow<List<ChecklistTaskEntity>>

    // ChecklistTaskResponsibleEntity operations
    @Insert
    suspend fun addChecklistResponsible(responsible: ChecklistTaskResponsibleEntity)

    @Query("DELETE FROM checklist_task_responsibles WHERE checklist_task_id = :taskId")
    suspend fun clearResponsiblesForChecklistTask(taskId: String)

    @Query("SELECT * FROM checklist_task_responsibles WHERE checklist_task_id = :taskId")
    fun getResponsiblesForChecklistTask(taskId: String): Flow<List<ChecklistTaskResponsibleEntity>>
}