package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.datasource.ChecklistDataSource
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.UUID
import ru.kolesnik.potok.core.network.retrofit.ExtendedRetrofitSyncFull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitChecklistDataSource @Inject constructor(
    private val networkApi: ExtendedRetrofitSyncFull
) : ChecklistDataSource {

    override suspend fun getChecklist(taskId: String): List<ChecklistTaskDTO> {
        return networkApi.checklistApi.getChecklist(taskId)
    }

    override suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        return networkApi.checklistApi.createChecklistTask(taskId, request)
    }

    override suspend fun updateChecklist(taskId: String, request: ChecklistRq): ChecklistDTO {
        return networkApi.checklistApi.updateChecklist(taskId, request)
    }

    override suspend fun updateChecklistTask(checklistTaskId: UUID, request: ChecklistTaskTitleRq): ChecklistTaskDTO {
        return networkApi.checklistApi.updateChecklistTask(checklistTaskId, request)
    }

    override suspend fun deleteChecklistTask(checklistTaskId: UUID) {
        networkApi.checklistApi.deleteChecklistTask(checklistTaskId)
    }

    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq): ChecklistTaskDTO {
        return networkApi.checklistApi.updateChecklistTaskDeadline(checklistTaskId, request)
    }

    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq): ChecklistTaskDTO {
        return networkApi.checklistApi.updateChecklistTaskResponsibles(checklistTaskId, request)
    }

    override suspend fun toggleChecklistTask(checklistTaskId: UUID, done: Boolean): ChecklistTaskDTO {
        return networkApi.checklistApi.toggleChecklistTask(checklistTaskId, done)
    }

    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) {
        networkApi.checklistApi.moveChecklistTask(request)
    }
}