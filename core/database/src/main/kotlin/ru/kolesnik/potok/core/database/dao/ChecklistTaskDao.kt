package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.entitys.ChecklistTaskResponsibleEntity
import java.time.OffsetDateTime
import java.util.UUID

@Dao
interface ChecklistTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkItem: ChecklistTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checkItems: List<ChecklistTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponsibles(responsibles: List<ChecklistTaskResponsibleEntity>)

    @Update
    suspend fun update(checkItem: ChecklistTaskEntity)

    @Delete
    suspend fun delete(checkItem: ChecklistTaskEntity)

    @Query("DELETE FROM checklist_tasks WHERE taskCardId = :taskId")
    suspend fun deleteByTaskId(taskId: UUID)

    @Query("DELETE FROM checklist_task_responsibles WHERE checklistTaskId = :checklistTaskId")
    suspend fun deleteResponsiblesByChecklistTaskId(checklistTaskId: UUID)

    @Query("DELETE FROM checklist_tasks")
    suspend fun deleteAll()

    @Query("SELECT * FROM checklist_tasks WHERE id = :id")
    suspend fun getById(id: UUID): ChecklistTaskEntity?

    @Query("SELECT * FROM checklist_tasks WHERE taskCardId = :taskId ORDER BY placement ASC")
    fun getByTaskId(taskId: UUID): Flow<List<ChecklistTaskEntity>>

    @Query("SELECT COUNT(*) FROM checklist_tasks WHERE taskCardId = :taskId AND done = 1")
    suspend fun getCompletedCount(taskId: UUID): Int

    @Query("SELECT COUNT(*) FROM checklist_tasks WHERE taskCardId = :taskId")
    suspend fun getTotalCount(taskId: UUID): Int

    @Query("UPDATE checklist_tasks SET done = :done WHERE id = :itemId")
    suspend fun updateDoneStatus(itemId: UUID, done: Boolean)

    @Query("UPDATE checklist_tasks SET placement = :newPosition WHERE id = :itemId")
    suspend fun updatePosition(itemId: UUID, newPosition: Int)

    @Query("UPDATE checklist_tasks SET title = :title WHERE id = :itemId")
    suspend fun updateTitle(itemId: UUID, title: String)

    @Query("UPDATE checklist_tasks SET deadline = :deadline WHERE id = :itemId")
    suspend fun updateDeadline(itemId: UUID, deadline: OffsetDateTime?)

    @Query("SELECT * FROM checklist_task_responsibles WHERE checklistTaskId = :checklistTaskId")
    suspend fun getResponsibles(checklistTaskId: UUID): List<ChecklistTaskResponsibleEntity>

    @Query("SELECT * FROM checklist_tasks WHERE id IN (SELECT checklistTaskId FROM checklist_task_responsibles WHERE employeeId = :employeeId)")
    suspend fun getItemsByResponsible(employeeId: String): List<ChecklistTaskEntity>
}