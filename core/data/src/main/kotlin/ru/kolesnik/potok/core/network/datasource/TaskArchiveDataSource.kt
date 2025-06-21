package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.NetworkTask

interface TaskArchiveDataSource {
    suspend fun getTaskArchive(limit: Int? = null, offset: Int? = null, sort: String? = null, status: String? = null): TaskArchivePageDTO
    suspend fun archiveTask(taskId: String)
    suspend fun deleteArchivedTasks(taskIds: String)
    suspend fun restoreTasks(request: TaskExternalIds)
    suspend fun getArchivedTaskDetails(taskId: String): NetworkTask
}