package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity
import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject

interface ChecklistRepository {
    suspend fun createChecklistTask(taskId: UUID, request: ChecklistTaskTitleRq): UUID
    suspend fun updateChecklistTaskTitle(itemId: UUID, request: ChecklistTaskTitleRq)
    suspend fun updateChecklistTaskStatus(itemId: UUID, done: Boolean)
    suspend fun deleteChecklistTask(itemId: UUID)
    suspend fun syncChecklistForTask(taskId: UUID)
    suspend fun getChecklistForTask(taskId: UUID): List<ChecklistTaskEntity>
}

class ChecklistRepositoryImpl @Inject constructor(
    private val api: ChecklistApi,
    private val checklistTaskDao: ChecklistTaskDao
) : ChecklistRepository {

    override suspend fun createChecklistTask(taskId: UUID, request: ChecklistTaskTitleRq): UUID {
        val response = api.createChecklistTask(taskId.toString(), request)
        val entity = response.toChecklistEntity(taskId)
        checklistTaskDao.insert(entity)
        return entity.id
    }

    override suspend fun updateChecklistTaskTitle(itemId: UUID, request: ChecklistTaskTitleRq) {
        api.updateChecklistTaskTitle(itemId, request)
        checklistTaskDao.updateTitle(itemId, request.title)
    }

    override suspend fun updateChecklistTaskStatus(itemId: UUID, done: Boolean) {
        api.updateChecklistTaskStatus(itemId, done)
        checklistTaskDao.updateDoneStatus(itemId, done)
    }

    override suspend fun deleteChecklistTask(itemId: UUID) {
        api.deleteChecklistTask(itemId)
        checklistTaskDao.delete(checklistTaskDao.getById(itemId) ?: return)
    }

    override suspend fun syncChecklistForTask(taskId: UUID) {
        val checklist = api.getChecklist(taskId.toString())
        val entities = checklist.map { it.toChecklistEntity(taskId) }
        checklistTaskDao.deleteByTaskId(taskId)
        checklistTaskDao.insertAll(entities)
    }

    override suspend fun getChecklistForTask(taskId: UUID): List<ChecklistTaskEntity> {
        return checklistTaskDao.getByTaskId(taskId)
    }
}

fun ChecklistTaskDTO.toChecklistEntity(taskId: UUID): ChecklistTaskEntity = ChecklistTaskEntity(
    id = id,
    taskCardId = taskId,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
)