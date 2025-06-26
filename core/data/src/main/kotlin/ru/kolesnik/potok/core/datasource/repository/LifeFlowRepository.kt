package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.model.extensions.toEntity
import java.util.UUID
import javax.inject.Inject

interface LifeFlowRepository {
    suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID
    suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq)
    suspend fun deleteLifeFlow(id: UUID)
    suspend fun moveLifeFlow(request: LifeFlowMoveDTO)
    suspend fun syncLifeFlows(lifeAreaId: UUID)
    suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowEntity>
}

class LifeFlowRepositoryImpl @Inject constructor(
    private val dataSource: SyncFullDataSource,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeFlowRepository {

    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID {
        // В демо-режиме просто возвращаем случайный UUID
        return UUID.randomUUID()
    }

    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq) {
        // В демо-режиме ничего не делаем
    }

    override suspend fun deleteLifeFlow(id: UUID) {
        taskDao.deleteByFlowId(id)
        lifeFlowDao.delete(lifeFlowDao.getById(id) ?: return)
    }

    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        // В демо-режиме ничего не делаем
    }

    override suspend fun syncLifeFlows(lifeAreaId: UUID) {
        // В демо-режиме получаем данные из локального источника
        val fullData = dataSource.getFull()
        val area = fullData.find { it.id.toString() == lifeAreaId.toString() }
        area?.flows?.let { flows ->
            val entities = flows.map { it.toEntity() }
            lifeFlowDao.deleteByAreaId(lifeAreaId)
            lifeFlowDao.insertAll(entities)
        }
    }

    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowEntity> {
        return lifeFlowDao.getByAreaId(lifeAreaId)
    }
}