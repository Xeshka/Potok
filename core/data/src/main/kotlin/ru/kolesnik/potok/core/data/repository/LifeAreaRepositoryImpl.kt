package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toModel
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
            val lifeAreas = lifeAreaApi.getLifeAreas()
            val entities = lifeAreas.map { it.toEntity() }
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
            val result = lifeAreaApi.createLifeArea(request)
            val entity = result.toEntity()
            lifeAreaDao.insert(entity)
            Result.Success(entity.toModel())
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
            val result = lifeAreaApi.updateLifeArea(java.util.UUID.fromString(id), request)
            val entity = result.toEntity()
            lifeAreaDao.update(entity)
            Result.Success(entity.toModel())
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