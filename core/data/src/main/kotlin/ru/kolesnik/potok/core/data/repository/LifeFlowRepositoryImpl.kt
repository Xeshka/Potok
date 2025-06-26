package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.data.util.toDomainModel
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
            // Здесь должна быть логика синхронизации потоков
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
            
            // ✅ Получаем DTO из API
            val resultDTO = lifeFlowApi.createLifeFlow(java.util.UUID.fromString(lifeAreaId), request)
            
            // ✅ Преобразуем DTO в Entity и сохраняем
            val entity = resultDTO.toEntity()
            lifeFlowDao.insert(entity)
            
            // ✅ Возвращаем доменную модель
            Result.Success(resultDTO.toDomainModel())
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
            
            // ✅ Получаем DTO из API
            val resultDTO = lifeFlowApi.updateLifeFlow(java.util.UUID.fromString(id), request)
            
            // ✅ Преобразуем DTO в Entity и обновляем
            val entity = resultDTO.toEntity()
            lifeFlowDao.update(entity)
            
            // ✅ Возвращаем доменную модель
            Result.Success(resultDTO.toDomainModel())
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