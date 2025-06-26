package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity
import java.time.OffsetDateTime
import java.util.UUID

@Dao
interface ChecklistTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkItem: ChecklistTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checkItems: List<ChecklistTaskEntity>)

    @Update
    suspend fun update(checkItem: ChecklistTaskEntity)

    @Delete
    suspend fun delete(checkItem: ChecklistTaskEntity)

    @Query("DELETE FROM checklist_tasks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM checklist_tasks WHERE taskCardId = :taskId")
    suspend fun deleteByTaskId(taskId: UUID)

    @Query("DELETE FROM checklist_tasks")
    suspend fun deleteAll()

    @Query("SELECT * FROM checklist_tasks WHERE id = :id")
    suspend fun getById(id: UUID): ChecklistTaskEntity?

    @Query("SELECT * FROM checklist_tasks WHERE taskCardId = :taskId ORDER BY placement ASC")
    suspend fun getByTaskId(taskId: UUID): List<ChecklistTaskEntity>

    @Query("SELECT * FROM checklist_tasks WHERE taskCardId = :taskId ORDER BY placement ASC")
    fun getChecklistTasksByTask(taskId: String): Flow<List<ChecklistTaskEntity>>

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

    @Query("UPDATE checklist_tasks SET responsibles = :responsibles WHERE id = :itemId")
    suspend fun updateResponsibles(itemId: UUID, responsibles: List<String>?)

    @Query("SELECT responsibles FROM checklist_tasks WHERE id = :itemId")
    suspend fun getResponsibles(itemId: UUID): List<String>?

    @Query("SELECT * FROM checklist_tasks WHERE :employeeId IN (responsibles)")
    suspend fun getItemsByResponsible(employeeId: String): List<ChecklistTaskEntity>
}