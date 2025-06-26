package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.entitys.TaskCommentEntity
import java.time.OffsetDateTime
import java.util.UUID

@Dao
interface TaskCommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: TaskCommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<TaskCommentEntity>)

    @Update
    suspend fun update(comment: TaskCommentEntity)

    @Delete
    suspend fun delete(comment: TaskCommentEntity)

    @Query("DELETE FROM task_comments WHERE id = :id")
    suspend fun deleteById(id: UUID)

    @Query("DELETE FROM task_comments WHERE taskCardId = :taskId")
    suspend fun deleteByTaskId(taskId: UUID)

    @Query("DELETE FROM task_comments")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_comments WHERE id = :id")
    suspend fun getById(id: UUID): TaskCommentEntity?

    @Query("SELECT * FROM task_comments WHERE taskCardId = :taskId")
    suspend fun getByTaskId(taskId: UUID): List<TaskCommentEntity>

    @Query("SELECT * FROM task_comments WHERE taskCardId = :taskId")
    fun getCommentsByTask(taskId: String): Flow<List<TaskCommentEntity>>

    @Query("SELECT * FROM task_comments WHERE parentCommentId = :parentId")
    suspend fun getReplies(parentId: UUID): List<TaskCommentEntity>

    @Query("SELECT COUNT(*) FROM task_comments WHERE taskCardId = :taskId")
    suspend fun getCommentCount(taskId: UUID): Int

    @Query("SELECT * FROM task_comments WHERE owner = :ownerId")
    suspend fun getByOwner(ownerId: String): List<TaskCommentEntity>

    @Query("UPDATE task_comments SET text = :text, updatedAt = :updatedAt WHERE id = :commentId")
    suspend fun updateText(commentId: UUID, text: String, updatedAt: OffsetDateTime)
}