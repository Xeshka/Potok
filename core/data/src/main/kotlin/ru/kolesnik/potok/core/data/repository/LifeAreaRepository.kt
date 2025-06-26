package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeAreaRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val dao: LifeAreaDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncLifeAreas() {
        try {
            val areas = api.getFullLifeAreas()
            val entities = areas.map { it.toEntity() }
            dao.deleteAll()
            dao.insertAll(entities)
        } catch (e: Exception) {
            // Handle error - maybe log it
            throw e
        }
    }

    override suspend fun createLifeArea(request: LifeAreaRq): UUID {
        val response = api.createLifeArea(request)
        val entity = response.toEntity()
        dao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) {
        val updated = api.updateLifeArea(id, request)
        dao.update(updated.toEntity())
    }

    override suspend fun deleteLifeArea(id: UUID) {
        api.deleteLifeArea(id)
        dao.getById(id)?.let { dao.delete(it) }
    }
}