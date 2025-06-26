package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeFlowRepositoryImpl @Inject constructor(
    private val lifeFlowApi: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao
) : LifeFlowRepository {

    override fun getLifeFlows(): Flow<List<LifeFlow>> {
        return lifeFlowDao.getAllLifeFlows().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getLifeFlowsByArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        return lifeFlowDao.getLifeFlowsByArea(lifeAreaId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun syncLifeFlows(): Result<Unit> {
        return try {
            when (val result = lifeFlowApi.getLifeFlows()) {
                is Result.Success -> {
                    val entities = result.data.map { it.toEntity() }
                    lifeFlowDao.insertAll(entities)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createLifeFlow(name: String, lifeAreaId: String, description: String?): Result<LifeFlow> {
        return try {
            when (val result = lifeFlowApi.createLifeFlow(name, lifeAreaId, description)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    lifeFlowDao.insert(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateLifeFlow(id: String, name: String, description: String?): Result<LifeFlow> {
        return try {
            when (val result = lifeFlowApi.updateLifeFlow(id, name, description)) {
                is Result.Success -> {
                    val entity = result.data.toEntity()
                    lifeFlowDao.update(entity)
                    Result.Success(entity.toModel())
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLifeFlow(id: String): Result<Unit> {
        return try {
            when (val result = lifeFlowApi.deleteLifeFlow(id)) {
                is Result.Success -> {
                    lifeFlowDao.deleteById(id)
                    Result.Success(Unit)
                }
                is Result.Error -> result
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}