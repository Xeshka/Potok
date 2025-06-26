package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.repository.FlowRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlowRepositoryImpl @Inject constructor(
    private val api: LifeFlowApi,
    private val dao: LifeFlowDao
) : FlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        return dao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncFlowsForArea(lifeAreaId: String) {
        try {
            val areaUuid = UUID.fromString(lifeAreaId)
            val flows = api.getLifeFlows(areaUuid)
            val entities = flows.map { it.toEntity() }
            dao.deleteByAreaId(areaUuid)
            dao.insertAll(entities)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createFlow(lifeAreaId: String, request: LifeFlowRq): String {
        val areaUuid = UUID.fromString(lifeAreaId)
        val response = api.createLifeFlow(areaUuid, request)
        val entity = response.toEntity()
        dao.insert(entity)
        return entity.id.toString()
    }

    override suspend fun updateFlow(flowId: String, request: LifeFlowRq) {
        val flowUuid = UUID.fromString(flowId)
        val updated = api.updateLifeFlow(flowUuid, request)
        dao.update(updated.toEntity())
    }

    override suspend fun deleteFlow(flowId: String) {
        val flowUuid = UUID.fromString(flowId)
        api.deleteLifeFlow(flowUuid)
        dao.getById(flowUuid)?.let { dao.delete(it) }
    }
}