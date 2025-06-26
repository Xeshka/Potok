package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload

interface TaskDataSource {
    suspend fun createTask(request: TaskRq): TaskRs
    suspend fun updateTask(taskId: String, request: PatchPayload)
    suspend fun getTaskId(taskId: String): Long
    suspend fun checkTaskAllowed(taskId: Long)
    suspend fun getTaskDetails(taskId: String): TaskRs
    suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq)
    suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq)
    suspend fun returnTask(taskId: String, assignee: String)
    suspend fun getTaskArchive(
        limit: Int? = null,
        offset: Int? = null,
        sort: String? = null,
        status: String? = null
    ): TaskArchivePageDTO
    suspend fun deleteTasksFromArchive(taskIds: String)
    suspend fun restoreTasksFromArchive(request: TaskExternalIds)
    suspend fun archiveTask(taskId: String)
    suspend fun getArchivedTaskDetails(taskId: String): TaskRs
}