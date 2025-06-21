package ru.kolesnik.potok.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.model.TaskCommentEntity

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateComment(comment: TaskCommentEntity)

    @Delete
    suspend fun deleteComment(comment: TaskCommentEntity)

    @Query("SELECT * FROM task_comments WHERE task_id = :taskId ORDER BY created_at ASC")
    fun getCommentsForTask(taskId: String): Flow<List<TaskCommentEntity>>

    @Query("SELECT * FROM task_comments WHERE id = :commentId")
    suspend fun getCommentById(commentId: String): TaskCommentEntity?

    @Query("DELETE FROM task_comments WHERE task_id = :taskId")
    suspend fun deleteAllCommentsForTask(taskId: String)

    @Query("SELECT * FROM task_comments WHERE text LIKE '%' || :query || '%' AND task_id = :taskId")
    suspend fun searchComments(taskId: String, query: String): List<TaskCommentEntity>
}