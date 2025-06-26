package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toDomain
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(request: LifeAreaRq): UUID
    suspend fun updateLifeArea(id: UUID, request: LifeAreaRq)
    suspend fun deleteLifeArea(id: UUID)
    suspend fun syncLifeAreas()
}

@Singleton
class LifeAreaRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createLifeArea(request: LifeAreaRq): UUID {
        val response = api.createLifeArea(request)
        val entity = response.toEntity()
        lifeAreaDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) {
        val updated = api.updateLifeArea(id, request)
        lifeAreaDao.update(updated.toEntity())
    }

    override suspend fun deleteLifeArea(id: UUID) {
        api.deleteLifeArea(id)
        // Каскадное удаление связанных данных
        lifeFlowDao.deleteByAreaId(id)
        taskDao.deleteByAreaId(id)
        lifeAreaDao.getById(id)?.let { lifeAreaDao.delete(it) }
    }

    override suspend fun syncLifeAreas() {
        val areas = api.getFullLifeAreas()
        val entities = areas.map { it.toEntity() }
        lifeAreaDao.deleteAll()
        lifeAreaDao.insertAll(entities)
    }
}