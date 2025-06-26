package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitChecklistDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistRepositoryImpl @Inject constructor(
    private val checklistApi: RetrofitChecklistDataSource,
    private val checklistTaskDao: ChecklistTaskDao
) : ChecklistRepository {

    override fun getChecklistTasks(taskId: String): Flow<List<ChecklistTask>> {
        return checklistTaskDao.getChecklistTasksByTask(taskId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun createChecklistTask(taskId: String, title: String): Result<ChecklistTask> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.ChecklistTaskTitleRq(title = title)
            val result = checklistApi.createChecklistTask(taskId, request)
            val entity = result.toEntity().copy(taskCardId = java.util.UUID.fromString(taskId))
            checklistTaskDao.insert(entity)
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateChecklistTask(id: String, title: String?, isCompleted: Boolean?): Result<ChecklistTask> {
        return try {
            val uuid = java.util.UUID.fromString(id)
            
            title?.let {
                val titleRequest = ru.kolesnik.potok.core.network.model.api.ChecklistTaskTitleRq(title = it)
                checklistApi.updateChecklistTaskTitle(uuid, titleRequest)
            }
            
            isCompleted?.let {
                checklistApi.updateChecklistTaskStatus(uuid, it)
            }
            
            // Получаем обновленную задачу из базы
            val entity = checklistTaskDao.getById(uuid)
            entity?.let {
                Result.Success(it.toModel())
            } ?: Result.Error(Exception("Checklist task not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteChecklistTask(id: String): Result<Unit> {
        return try {
            val uuid = java.util.UUID.fromString(id)
            checklistApi.deleteChecklistTask(uuid)
            checklistTaskDao.deleteById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun toggleChecklistTask(id: String): Result<ChecklistTask> {
        return try {
            val uuid = java.util.UUID.fromString(id)
            val entity = checklistTaskDao.getById(uuid)
            entity?.let {
                val newStatus = !it.done
                val result = checklistApi.updateChecklistTaskStatus(uuid, newStatus)
                val updatedEntity = result.toEntity().copy(taskCardId = it.taskCardId)
                checklistTaskDao.update(updatedEntity)
                Result.Success(updatedEntity.toModel())
            } ?: Result.Error(Exception("Checklist task not found"))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}