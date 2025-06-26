package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskComment
import ru.kolesnik.potok.core.network.result.Result

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTasksByFlow(flowId: String): Flow<List<Task>>
    fun getTask(id: String): Flow<Task?>
    suspend fun syncTasks(): Result<Unit>
    suspend fun createTask(
        title: String,
        description: String?,
        lifeFlowId: String,
        assigneeIds: List<String> = emptyList(),
        deadline: String? = null,
        isImportant: Boolean = false
    ): Result<Task>
    suspend fun updateTask(
        id: String,
        title: String?,
        description: String?,
        assigneeIds: List<String>?,
        deadline: String?,
        isImportant: Boolean?
    ): Result<Task>
    suspend fun deleteTask(id: String): Result<Unit>
    suspend fun completeTask(id: String): Result<Task>
    suspend fun archiveTask(id: String): Result<Unit>
    
    // Comments
    fun getTaskComments(taskId: String): Flow<List<TaskComment>>
    suspend fun addComment(taskId: String, text: String): Result<TaskComment>
    suspend fun updateComment(commentId: String, text: String): Result<TaskComment>
    suspend fun deleteComment(commentId: String): Result<Unit>
}