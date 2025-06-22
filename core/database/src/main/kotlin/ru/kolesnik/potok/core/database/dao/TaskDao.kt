package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import ru.kolesnik.potok.core.database.entitys.TaskEntity
import java.time.OffsetDateTime
import java.util.UUID

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query("DELETE FROM tasks WHERE lifeAreaId = :areaId")
    suspend fun deleteByAreaId(areaId: UUID)

    @Query("DELETE FROM tasks WHERE flowId = :flowId")
    suspend fun deleteByFlowId(flowId: UUID)

    @Query("SELECT * FROM tasks WHERE cardId = :cardId")
    suspend fun getById(cardId: UUID): TaskEntity?

    @Query("SELECT * FROM tasks WHERE externalId = :externalId")
    suspend fun getByExternalId(externalId: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE deletedAt IS NULL")
    suspend fun getActiveTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE deletedAt IS NOT NULL")
    suspend fun getArchivedTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE lifeAreaId = :areaId AND deletedAt IS NULL")
    suspend fun getByAreaId(areaId: UUID): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE flowId = :flowId AND deletedAt IS NULL")
    suspend fun getByFlowId(flowId: UUID): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE flowId = :flowId AND lifeAreaPlacement = :position AND deletedAt IS NULL")
    suspend fun getByFlowAndPosition(flowId: UUID, position: Int): List<TaskEntity>

    @Query("UPDATE tasks SET deletedAt = :deletedAt WHERE cardId = :cardId")
    suspend fun markAsArchived(cardId: UUID, deletedAt: OffsetDateTime)

    @Query("UPDATE tasks SET deletedAt = NULL WHERE cardId = :cardId")
    suspend fun restoreFromArchive(cardId: UUID)

    @Query("SELECT COUNT(*) FROM tasks WHERE flowId = :flowId AND deletedAt IS NULL")
    suspend fun countByFlow(flowId: UUID): Int

    @Query("UPDATE tasks SET flowId = :flowId, flowPlacement = :position WHERE cardId = :taskId")
    suspend fun moveToFlow(taskId: UUID, flowId: UUID, position: Int)

    @Query("UPDATE tasks SET lifeAreaId = :areaId, lifeAreaPlacement = :position WHERE cardId = :taskId")
    suspend fun moveToArea(taskId: UUID, areaId: UUID, position: Int)
}