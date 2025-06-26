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
        return lifeFlowDao.getByAreaIdFlow(java.util.UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun syncLifeFlows(): Result<Unit> {
        return try {
            // Получаем все области жизни для синхронизации их потоков
            val lifeAreas = ru.kolesnik.potok.core.database.dao.LifeAreaDao::class.java
            // Здесь нужно получить все области и для каждой синхронизировать потоки
            // Пока упрощенная версия
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createLifeFlow(name: String, lifeAreaId: String, description: String?): Result<LifeFlow> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.LifeFlowRq(
                title = name,
                style = description
            )
            val result = lifeFlowApi.createLifeFlow(java.util.UUID.fromString(lifeAreaId), request)
            val entity = result.toEntity()
            lifeFlowDao.insert(entity)
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateLifeFlow(id: String, name: String, description: String?): Result<LifeFlow> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.LifeFlowRq(
                title = name,
                style = description
            )
            val result = lifeFlowApi.updateLifeFlow(java.util.UUID.fromString(id), request)
            val entity = result.toEntity()
            lifeFlowDao.update(entity)
            Result.Success(entity.toModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLifeFlow(id: String): Result<Unit> {
        return try {
            lifeFlowApi.deleteLifeFlow(java.util.UUID.fromString(id))
            lifeFlowDao.deleteByAreaId(java.util.UUID.fromString(id))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}