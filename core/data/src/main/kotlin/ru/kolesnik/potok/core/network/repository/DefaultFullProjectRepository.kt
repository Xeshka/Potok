package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.database.dao.ChecklistDao
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.model.LifeAreaEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoRecipientEntity
import ru.kolesnik.potok.core.database.model.LifeFlowEntity
import ru.kolesnik.potok.core.database.model.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.model.TaskEntity
import ru.kolesnik.potok.core.database.model.TaskExternalId
import ru.kolesnik.potok.core.database.model.TaskPayloadEntity
import ru.kolesnik.potok.core.model.EmployeeId
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.ChecklistEntities
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.toEntities
import javax.inject.Inject

class DefaultFullProjectRepository @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao,
    private val checklistDao: ChecklistDao,
) : FullProjectRepository {

    override suspend fun sync() {
        val fullRq = syncFullDataSource.getFull()
            .map(NetworkLifeArea::toEntities)

        val lifeAreasDeferred = fullRq.map { it.lifeArea }.syncLifeAreaEntity()
        val lifeAreaInfoDeferred =
            fullRq.mapNotNull { it.sharedInfo }.syncLifeAreaSharedInfoEntity()
        val sharedRecipientsDeferred =
            fullRq.flatMap { it.sharedRecipients }.syncLifeAreaSharedInfoRecipientEntity()

        val flowsEn = fullRq.flatMap { it.flows }
        val flowsDeferred = flowsEn.map { it.flow }.syncLifeFlowEntity()

        val tasksEn = flowsEn.flatMap { it.tasks }

        val tasksDeferred = tasksEn.map { it.task }.syncTaskEntity()
        val payloadsDeferred = tasksEn.mapNotNull { it.payload }.syncTaskPayloadEntity()
        val checklistsDeferred = tasksEn.mapNotNull { it.checklists }.syncChecklistEntities()
        val assignees = tasksEn.map { it.assignees?.syncTaskAssigneeEntity(it.task.id) }
    }

    override suspend fun getEmployee(employees: List<EmployeeId>): List<EmployeeResponse> {
        return syncFullDataSource.getEmployee(employees, true)
    }

    private suspend fun List<LifeAreaEntity>.syncLifeAreaEntity() =
        onEach { lifeAreaDao.insertOrUpdateLifeArea(it) }

    private suspend fun List<LifeAreaSharedInfoEntity>.syncLifeAreaSharedInfoEntity() =
        onEach { lifeAreaDao.insertOrUpdateSharedInfo(it) }

    private suspend fun List<LifeAreaSharedInfoRecipientEntity>.syncLifeAreaSharedInfoRecipientEntity() =
        onEach { lifeAreaDao.addSharedInfoRecipient(it) }

    private suspend fun List<LifeFlowEntity>.syncLifeFlowEntity() =
        onEach { lifeFlowDao.insertOrUpdateFlow(it) }

    private suspend fun List<TaskEntity>.syncTaskEntity() =
        onEach { taskDao.insertOrUpdateTask(it) }

    private suspend fun List<TaskPayloadEntity>.syncTaskPayloadEntity() =
        onEach { taskDao.insertOrUpdateTaskPayload(it) }

    private suspend fun List<TaskAssigneeEntity>.syncTaskAssigneeEntity(taskId: TaskExternalId) =
        taskDao.clearAssigneesForTask(taskId).also {
            onEach {
                taskDao.addTaskAssignee(it)
            }
        }

    private suspend fun List<ChecklistEntities>.syncChecklistEntities() =
        onEach { check ->
            check.checklistTasks.onEach { checklistDao.insertOrUpdateChecklistTask(it) }
            check.responsibles.onEach { checklistDao.addChecklistResponsible(it) }
        }

}