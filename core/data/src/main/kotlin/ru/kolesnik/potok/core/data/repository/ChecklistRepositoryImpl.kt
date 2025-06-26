package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChecklistRepositoryImpl @Inject constructor(
    private val checklistApi: ChecklistApi,
    private val checklistTaskDao: ChecklistTaskDao
) : ChecklistRepository {

    override fun getChecklistTasks(taskId: String): Flow<List<ChecklistTask>> {
        return checklistTaskDao.getChecklistTasksByTask(taskId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun createChecklistTask(taskId: String, title: String): Result<ChecklistTask> {
        return try {
            when (val result = checklistApi.createChecklistTask(taskId, title)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    checklistTaskDao.insert(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateChecklistTask(id: String, title: String?, isCompleted: Boolean?): Result<ChecklistTask> {
        return try {
            when (val result = checklistApi.updateChecklistTask(id, title, isCompleted)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    checklistTaskDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteChecklistTask(id: String): Result<Unit> {
        return try {
            when (val result = checklistApi.deleteChecklistTask(id)) {
                is Result.Success -> {
                    checklistTaskDao.deleteById(id)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun toggleChecklistTask(id: String): Result<ChecklistTask> {
        return try {
            when (val result = checklistApi.toggleChecklistTask(id)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    checklistTaskDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}