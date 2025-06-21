package ru.kolesnik.potok.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.model.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.model.TaskEntity
import ru.kolesnik.potok.core.database.model.TaskPayloadEntity


@Dao
interface TaskDao {
    @Query("UPDATE tasks SET flow_id = :flowId WHERE id = :taskId")
    suspend fun closeTask(taskId: String, flowId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    @Query("DELETE FROM task_payloads WHERE id = :taskId")
    suspend fun deletePayloadTask(taskId: String)

    @Query("DELETE FROM task_assignees WHERE task_id = :taskId")
    suspend fun deleteAssigneesTask(taskId: String)

    @Query("SELECT * FROM tasks WHERE life_area_id = :areaId ORDER BY life_area_placement ASC")
    fun getTasksForArea(areaId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE flow_id = :flowId ORDER BY flow_placement ASC ")
    fun getTasksForFlow(flowId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateTaskPayload(payload: TaskPayloadEntity)

    @Query("SELECT * FROM task_payloads WHERE task_id = :taskId")
    suspend fun getPayloadForTask(taskId: String): TaskPayloadEntity?

    @Query("SELECT deadline FROM task_payloads WHERE task_id = :taskId")
    fun getDeadline(taskId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTaskAssignee(assignee: TaskAssigneeEntity)

    @Query("DELETE FROM task_assignees WHERE task_id = :taskId")
    suspend fun clearAssigneesForTask(taskId: String)

    @Query("SELECT * FROM task_assignees WHERE task_id = :taskId")
    fun getAssigneesForTask(taskId: String): Flow<List<TaskAssigneeEntity>>
}