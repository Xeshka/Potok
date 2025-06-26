package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.repository.FlowRepository
import java.util.UUID
import javax.inject.Inject

class FlowRepositoryImpl @Inject constructor(
    private val api: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao,
    private val syncRepository: SyncRepository
) : FlowRepository {
    
    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        return lifeFlowDao.getByAreaIdFlow(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { entity ->
                LifeFlow(
                    id = entity.id.toString(),
                    areaId = entity.areaId.toString(),
                    title = entity.title,
                    style = entity.style,
                    placement = entity.placement,
                    status = entity.status ?: ru.kolesnik.potok.core.model.FlowStatus.NEW
                )
            }
        }
    }
    
    override suspend fun createFlow(lifeAreaId: String, title: String, style: String?): String {
        val request = LifeFlowRq(
            title = title,
            style = style,
            placement = null
        )
        
        val response = api.createLifeFlow(UUID.fromString(lifeAreaId), request)
        syncRepository.syncLifeFlows(UUID.fromString(lifeAreaId))
        
        return response.id.toString()
    }
    
    override suspend fun updateFlow(flowId: String, title: String, style: String?) {
        val request = LifeFlowRq(
            title = title,
            style = style,
            placement = null
        )
        
        api.updateLifeFlow(UUID.fromString(flowId), request)
        
        // Находим сферу жизни для этого потока
        val flow = lifeFlowDao.getById(UUID.fromString(flowId))
        if (flow != null) {
            syncRepository.syncLifeFlows(flow.areaId)
        }
    }
    
    override suspend fun deleteFlow(flowId: String) {
        val flow = lifeFlowDao.getById(UUID.fromString(flowId))
        if (flow != null) {
            api.deleteLifeFlow(UUID.fromString(flowId))
            lifeFlowDao.delete(flow)
        }
    }
}