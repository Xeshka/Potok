package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.repository.FlowRepository
import java.util.UUID
import javax.inject.Inject

class FlowRepositoryImpl @Inject constructor(
    private val lifeFlowApi: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao
) : FlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        val areaUuid = UUID.fromString(lifeAreaId)
        return lifeFlowDao.getByAreaIdFlow(areaUuid).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createFlow(lifeAreaId: UUID, title: String, style: String?): UUID {
        val request = LifeFlowRq(
            title = title,
            style = style ?: "style1"
        )
        
        val response = lifeFlowApi.createLifeFlow(lifeAreaId, request)
        val entity = LifeFlowEntity(
            id = response.id,
            areaId = response.areaId,
            title = response.title,
            style = response.style,
            placement = response.placement,
            status = response.status
        )
        
        lifeFlowDao.insert(entity)
        return response.id
    }

    override suspend fun updateFlow(flowId: UUID, title: String, style: String?): LifeFlow {
        val request = LifeFlowRq(
            title = title,
            style = style
        )
        
        val response = lifeFlowApi.updateLifeFlow(flowId, request)
        val entity = LifeFlowEntity(
            id = response.id,
            areaId = response.areaId,
            title = response.title,
            style = response.style,
            placement = response.placement,
            status = response.status
        )
        
        lifeFlowDao.update(entity)
        return entity.toDomain()
    }

    override suspend fun deleteFlow(flowId: UUID) {
        lifeFlowApi.deleteLifeFlow(flowId)
        val entity = lifeFlowDao.getById(flowId)
        if (entity != null) {
            lifeFlowDao.delete(entity)
        }
    }
    
    private fun LifeFlowEntity.toDomain(): LifeFlow = LifeFlow(
        id = id.toString(),
        areaId = areaId.toString(),
        title = title,
        style = style,
        placement = placement,
        status = status ?: ru.kolesnik.potok.core.model.FlowStatus.NEW
    )
}