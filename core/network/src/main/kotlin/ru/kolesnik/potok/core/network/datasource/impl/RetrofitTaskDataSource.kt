package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.datasource.TaskDataSource
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import javax.inject.Inject

class RetrofitTaskDataSource @Inject constructor(
    private val api: TaskApi
) : TaskDataSource {
    override suspend fun createTask(request: TaskRq) = api.createTask(request)
    override suspend fun updateTask(taskId: String, request: PatchPayload) = api.updateTask(taskId, request)
    override suspend fun getTaskId(taskId: String) = api.getTaskId(taskId)
    override suspend fun checkTaskAllowed(taskId: Long) = api.checkTaskAllowed(taskId)
    override suspend fun getTaskDetails(taskId: String) = api.getTaskDetails(taskId)
    override suspend fun moveTaskToFlow(taskId: String, request: FlowPositionRq) = api.moveTaskToFlow(taskId, request)
    override suspend fun moveTaskToLifeArea(taskId: String, request: LifeAreaPositionRq) = api.moveTaskToLifeArea(taskId, request)
    override suspend fun returnTask(taskId: String, assignee: String) = api.returnTask(taskId, assignee)
    override suspend fun getTaskArchive(limit: Int?, offset: Int?, sort: String?, status: String?) = api.getTaskArchive(limit, offset, sort, status)
    override suspend fun deleteTasksFromArchive(taskIds: String) = api.deleteTasksFromArchive(taskIds)
    override suspend fun restoreTasksFromArchive(request: TaskExternalIds) = api.restoreTasksFromArchive(request)
    override suspend fun archiveTask(taskId: String) = api.archiveTask(taskId)
    override suspend fun getArchivedTaskDetails(taskId: String) = api.getArchivedTaskDetails(taskId)
}