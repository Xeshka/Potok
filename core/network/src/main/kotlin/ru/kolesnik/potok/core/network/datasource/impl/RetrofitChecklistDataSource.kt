package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.datasource.ChecklistDataSource
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject

class RetrofitChecklistDataSource @Inject constructor(
    private val api: ChecklistApi
) : ChecklistDataSource {
    override suspend fun getChecklist(taskId: String) = api.getChecklist(taskId)
    override suspend fun createChecklist(taskId: String, request: ChecklistRq) = api.createChecklist(taskId, request)
    override suspend fun moveChecklistTask(request: ChecklistTaskMoveRq) = api.moveChecklistTask(request)
    override suspend fun updateChecklistTaskTitle(checklistTaskId: UUID, request: ChecklistTaskTitleRq) = api.updateChecklistTaskTitle(checklistTaskId, request)
    override suspend fun deleteChecklistTask(checklistTaskId: UUID) = api.deleteChecklistTask(checklistTaskId)
    override suspend fun updateChecklistTaskDeadline(checklistTaskId: UUID, request: ChecklistTaskDeadlineRq) = api.updateChecklistTaskDeadline(checklistTaskId, request)
    override suspend fun updateChecklistTaskResponsibles(checklistTaskId: UUID, request: ChecklistTaskResponsiblesRq) = api.updateChecklistTaskResponsibles(checklistTaskId, request)
    override suspend fun updateChecklistTaskStatus(checklistTaskId: UUID, done: Boolean) = api.updateChecklistTaskStatus(checklistTaskId, done)
    override suspend fun createChecklistTask(taskId: String, request: ChecklistTaskTitleRq) = api.createChecklistTask(taskId, request)
}