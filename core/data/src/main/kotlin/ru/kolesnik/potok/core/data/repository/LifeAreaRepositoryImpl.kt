package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
import ru.kolesnik.potok.core.data.util.toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeAreaRepositoryImpl @Inject constructor(
    private val lifeAreaApi: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAllFlow().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun syncLifeAreas(): Result<Unit> {
        return try {
            // ✅ Получаем DTO из API
            val lifeAreaDTOs = lifeAreaApi.getLifeAreas()
            
            // ✅ Преобразуем DTO напрямую в Entity (оптимизация)
            val entities = lifeAreaDTOs.map { it.toEntity() }
            lifeAreaDao.insertAll(entities)
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createLifeArea(name: String, description: String?): Result<LifeArea> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.LifeAreaRq(
                title = name,
                style = description,
                isTheme = false,
                onlyPersonal = true
            )
            
            // ✅ Получаем DTO из API
            val resultDTO = lifeAreaApi.createLifeArea(request)
            
            // ✅ Преобразуем DTO в Entity и сохраняем
            val entity = resultDTO.toEntity()
            lifeAreaDao.insert(entity)
            
            // ✅ Возвращаем доменную модель
            Result.Success(resultDTO.toDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateLifeArea(id: String, name: String, description: String?): Result<LifeArea> {
        return try {
            val request = ru.kolesnik.potok.core.network.model.api.LifeAreaRq(
                title = name,
                style = description,
                isTheme = false,
                onlyPersonal = true
            )
            
            // ✅ Получаем DTO из API
            val resultDTO = lifeAreaApi.updateLifeArea(java.util.UUID.fromString(id), request)
            
            // ✅ Преобразуем DTO в Entity и обновляем
            val entity = resultDTO.toEntity()
            lifeAreaDao.update(entity)
            
            // ✅ Возвращаем доменную модель
            Result.Success(resultDTO.toDomainModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLifeArea(id: String): Result<Unit> {
        return try {
            lifeAreaApi.deleteLifeArea(java.util.UUID.fromString(id))
            lifeAreaDao.deleteById(java.util.UUID.fromString(id))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}