package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.result.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeAreaRepository: LifeAreaRepository,
    private val lifeFlowRepository: LifeFlowRepository,
    private val taskRepository: TaskRepository
) : SyncRepository {

    override suspend fun syncAll(): Result<Unit> {
        return try {
            // Синхронизируем все данные по очереди
            when (val lifeAreasResult = lifeAreaRepository.syncLifeAreas()) {
                is Result.Error -> return lifeAreasResult
                is Result.Success -> Unit
            }

            when (val lifeFlowsResult = lifeFlowRepository.syncLifeFlows()) {
                is Result.Error -> return lifeFlowsResult
                is Result.Success -> Unit
            }

            when (val tasksResult = taskRepository.syncTasks()) {
                is Result.Error -> return tasksResult
                is Result.Success -> Unit
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncFullProject(): Result<Unit> {
        return try {
            syncFullDataSource.syncFullData()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}