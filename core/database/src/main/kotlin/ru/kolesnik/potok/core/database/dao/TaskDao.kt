package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.entitys.TaskEntity
import ru.kolesnik.potok.core.database.entitys.TaskPayloadEntity
import java.util.UUID

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayload(payload: TaskPayloadEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Update
    suspend fun updatePayload(payload: TaskPayloadEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query("DELETE FROM tasks WHERE lifeAreaId = :areaId")
    suspend fun deleteByAreaId(areaId: UUID)

    @Query("DELETE FROM tasks WHERE flowId = :flowId")
    suspend fun deleteByFlowId(flowId: UUID)

    @Query("DELETE FROM task_payloads WHERE taskId = :taskId")
    suspend fun deletePayloadByTaskId(taskId: UUID)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: UUID): TaskEntity?

    @Query("SELECT * FROM task_payloads WHERE taskId = :taskId")
    suspend fun getPayloadByTaskId(taskId: UUID): TaskPayloadEntity?

    @Query("SELECT * FROM tasks WHERE lifeAreaId = :areaId")
    fun getByAreaId(areaId: UUID): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE flowId = :flowId")
    fun getByFlowId(flowId: UUID): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE flowId = :flowId AND lifeAreaPlacement = :position")
    suspend fun getByFlowAndPosition(flowId: UUID, position: Int): List<TaskEntity>

    @Query("SELECT COUNT(*) FROM tasks WHERE flowId = :flowId")
    suspend fun countByFlow(flowId: UUID): Int

    @Query("UPDATE tasks SET flowId = :flowId, flowPlacement = :position WHERE id = :taskId")
    suspend fun moveToFlow(taskId: UUID, flowId: UUID, position: Int)

    @Query("UPDATE tasks SET lifeAreaId = :areaId, lifeAreaPlacement = :position WHERE id = :taskId")
    suspend fun moveToArea(taskId: UUID, areaId: UUID, position: Int)
}