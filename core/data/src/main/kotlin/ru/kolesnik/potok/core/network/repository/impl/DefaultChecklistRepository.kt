package ru.kolesnik.potok.core.network.repository.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.datasource.ChecklistDataSource
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import ru.kolesnik.potok.core.network.repository.ChecklistRepository
import ru.kolesnik.potok.core.network.result.Result
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

class DefaultChecklistRepository @Inject constructor(
    private val checklistDataSource: ChecklistDataSource,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ChecklistRepository {

    override suspend fun getChecklist(taskId: String): Result<List<ChecklistTask>> = withContext(ioDispatcher) {
        try {
            val response = checklistDataSource.getChecklist(taskId)
            Result.Success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createChecklistTask(taskId: String, title: String): Result<ChecklistTask> = withContext(ioDispatcher) {
        try {
            val request = ChecklistTaskTitleRq(title)
            val response = checklistDataSource.createChecklistTask(taskId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateChecklistTask(checklistTaskId: UUID, title: String): Result<ChecklistTask> = withContext(ioDispatcher) {
        try {
            val request = ChecklistTaskTitleRq(title)
            val response = checklistDataSource.updateChecklistTask(checklistTaskId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteChecklistTask(checklistTaskId: UUID): Result<Unit> = withContext(ioDispatcher) {
        try {
            checklistDataSource.deleteChecklistTask(checklistTaskId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, deadline: OffsetDateTime?): Result<ChecklistTask> = withContext(ioDispatcher) {
        try {
            val request = ChecklistTaskDeadlineRq(deadline?.toString())
            val response = checklistDataSource.updateChecklistTaskDeadline(checklistTaskId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, responsibles: List<String>?): Result<ChecklistTask> = withContext(ioDispatcher) {
        try {
            val request = ChecklistTaskResponsiblesRq(responsibles)
            val response = checklistDataSource.updateChecklistTaskResponsibles(checklistTaskId, request)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun toggleChecklistTask(checklistTaskId: UUID, done: Boolean): Result<ChecklistTask> = withContext(ioDispatcher) {
        try {
            val response = checklistDataSource.toggleChecklistTask(checklistTaskId, done)
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun moveChecklistTask(checklistTaskId: UUID, placement: Int?): Result<Unit> = withContext(ioDispatcher) {
        try {
            val request = ChecklistTaskMoveRq(checklistTaskId, placement)
            checklistDataSource.moveChecklistTask(request)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun ChecklistTaskDTO.toDomain(): ChecklistTask {
        return ChecklistTask(
            id = UUID.fromString(this.id),
            title = this.title,
            done = this.done ?: false,
            placement = this.placement ?: 0,
            responsibles = this.responsibles ?: emptyList(),
            deadline = this.deadline?.let { OffsetDateTime.parse(it) }
        )
    }
}