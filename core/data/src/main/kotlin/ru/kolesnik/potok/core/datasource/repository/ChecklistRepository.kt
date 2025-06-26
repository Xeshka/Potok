package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.repository.ChecklistRepository
import java.util.UUID
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(
    private val api: ChecklistApi,
    private val checklistTaskDao: ChecklistTaskDao
) : ChecklistRepository {

    override suspend fun getChecklistForTask(taskId: String): List<ChecklistTask> {
        return checklistTaskDao.getByTaskId(UUID.fromString(taskId)).map { it.toDomain() }
    }

    override suspend fun createChecklistItem(taskId: String, item: ChecklistTask): ChecklistTask {
        val request = ChecklistTaskTitleRq(item.title)
        val response = api.createChecklistTask(taskId, request)
        val entity = response.toEntity(UUID.fromString(taskId))
        checklistTaskDao.insert(entity)
        return entity.toDomain()
    }

    override suspend fun updateChecklistItem(item: ChecklistTask): ChecklistTask {
        val request = ChecklistTaskTitleRq(item.title)
        val response = api.updateChecklistTaskTitle(item.id, request)
        val entity = response.toEntity(UUID.randomUUID()) // taskId не важен для обновления
        checklistTaskDao.update(entity)
        return entity.toDomain()
    }

    override suspend fun deleteChecklistItem(itemId: String) {
        api.deleteChecklistTask(UUID.fromString(itemId))
        checklistTaskDao.getById(UUID.fromString(itemId))?.let {
            checklistTaskDao.delete(it)
        }
    }
}

private fun ChecklistTaskDTO.toEntity(taskId: UUID): ChecklistTaskEntity = ChecklistTaskEntity(
    id = id,
    taskCardId = taskId,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
)

private fun ChecklistTaskEntity.toDomain(): ChecklistTask = ChecklistTask(
    id = id,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles?.map { it } ?: emptyList(),
    deadline = deadline
)