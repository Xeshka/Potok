package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.FlowRepository
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.model.extensions.toEntity
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import java.util.UUID
import javax.inject.Inject

class FlowRepositoryImpl @Inject constructor(
    private val api: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : FlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        val areaUuid = UUID.fromString(lifeAreaId)
        return lifeFlowDao.getByAreaId(areaUuid).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createFlow(lifeAreaId: UUID, title: String, style: String): UUID {
        val request = LifeFlowRq(title = title, style = style)
        val response = api.createLifeFlow(lifeAreaId, request)
        val entity = response.toEntity()
        lifeFlowDao.insert(entity)
        return entity.id
    }

    override suspend fun updateFlow(id: UUID, title: String, style: String) {
        val request = LifeFlowRq(title = title, style = style)
        val updated = api.updateLifeFlow(id, request)
        lifeFlowDao.update(updated.toEntity())
    }

    override suspend fun deleteFlow(id: UUID) {
        api.deleteLifeFlow(id)
        taskDao.deleteByFlowId(id)
        lifeFlowDao.getById(id)?.let { lifeFlowDao.delete(it) }
    }

    override suspend fun syncFlows(lifeAreaId: UUID) {
        val flows = api.getLifeFlows(lifeAreaId)
        val entities = flows.map { it.toEntity() }
        lifeFlowDao.deleteByAreaId(lifeAreaId)
        lifeFlowDao.insertAll(entities)
    }
}