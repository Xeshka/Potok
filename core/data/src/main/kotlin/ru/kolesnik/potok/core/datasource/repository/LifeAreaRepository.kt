package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.model.extensions.toEntity
import java.util.UUID
import javax.inject.Inject

class LifeAreaRepositoryImpl @Inject constructor(
    private val dataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeAreaRepository {

    override suspend fun createLifeArea(request: LifeAreaRq): UUID {
        // В демо-режиме просто возвращаем случайный UUID
        return UUID.randomUUID()
    }

    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) {
        // В демо-режиме ничего не делаем
    }

    override suspend fun deleteLifeArea(id: UUID) {
        lifeFlowDao.deleteByAreaId(id)
        taskDao.deleteByAreaId(id)
        lifeAreaDao.delete(lifeAreaDao.getById(id) ?: return)
    }

    override suspend fun syncLifeAreas() {
        val areas = dataSource.gtFullNew()
        val entities = areas.map { it.toEntity() }
        lifeAreaDao.deleteAll()
        lifeAreaDao.insertAll(entities)
    }

    override suspend fun getLifeAreas(): List<LifeAreaEntity> {
        return lifeAreaDao.getAll()
    }
}