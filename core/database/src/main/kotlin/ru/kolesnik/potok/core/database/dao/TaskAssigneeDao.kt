package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity
import java.util.UUID

@Dao
interface TaskAssigneeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assignee: TaskAssigneeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assignees: List<TaskAssigneeEntity>)

    @Update
    suspend fun update(assignee: TaskAssigneeEntity)

    @Delete
    suspend fun delete(assignee: TaskAssigneeEntity)

    @Query("DELETE FROM task_assignees WHERE taskCardId = :taskId")
    suspend fun deleteByTaskId(taskId: UUID)

    @Query("DELETE FROM task_assignees")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_assignees WHERE taskCardId = :taskId")
    suspend fun getByTaskId(taskId: UUID): List<TaskAssigneeEntity>

    @Query("SELECT * FROM task_assignees WHERE employeeId = :employeeId")
    suspend fun getByEmployeeId(employeeId: String): List<TaskAssigneeEntity>

    @Query("SELECT * FROM task_assignees WHERE taskCardId = :taskId AND employeeId = :employeeId")
    suspend fun getByTaskAndEmployee(taskId: UUID, employeeId: String): TaskAssigneeEntity?

    @Query("UPDATE task_assignees SET complete = :complete WHERE taskCardId = :taskId AND employeeId = :employeeId")
    suspend fun updateCompletionStatus(taskId: UUID, employeeId: String, complete: Boolean)
}