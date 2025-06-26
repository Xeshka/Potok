package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.LifeAreaRepository
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.SyncFullDataSource
import javax.inject.Inject

internal class LifeAreaRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncLifeAreas() {
        try {
            val networkData = syncDataSource.gtFullNew()
            val entities = networkData.map { it.toEntity() }
            
            // Clear and insert new data
            lifeAreaDao.deleteAll()
            lifeAreaDao.insertAll(entities)
        } catch (e: Exception) {
            // Log error but don't throw - let cached data be used
            println("Failed to sync life areas: ${e.message}")
        }
    }

    override suspend fun getLifeAreaById(id: String): LifeArea? {
        return lifeAreaDao.getById(java.util.UUID.fromString(id))?.toDomain()
    }
}