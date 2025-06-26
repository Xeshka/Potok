package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.retrofit.LifeFlowApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeFlowApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeFlowApi {
    
    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        // No-op in demo mode
    }
    
    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowDTO> {
        return dataSource.getFullLifeAreas()
            .find { it.id == lifeAreaId }
            ?.flows ?: emptyList()
    }
    
    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Return a mock flow for demo mode
        return LifeFlowDTO(
            id = UUID.randomUUID(),
            areaId = lifeAreaId,
            title = request.title,
            style = request.style ?: "style1",
            placement = request.placement,
            status = FlowStatus.NEW
        )
    }
    
    override suspend fun collocateLifeFlows(lifeAreaId: UUID, flowIds: List<UUID>) {
        // No-op in demo mode
    }
    
    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Find the flow in our demo data
        val allAreas = dataSource.getFullLifeAreas()
        for (area in allAreas) {
            val flow = area.flows?.find { it.id == id }
            if (flow != null) {
                // Return an updated version
                return flow.copy(
                    title = request.title,
                    style = request.style ?: flow.style,
                    placement = request.placement ?: flow.placement
                )
            }
        }
        
        // If not found, return a mock
        return LifeFlowDTO(
            id = id,
            areaId = UUID.randomUUID(), // We don't know the area ID here
            title = request.title,
            style = request.style ?: "style1",
            placement = request.placement,
            status = FlowStatus.NEW
        )
    }
    
    override suspend fun deleteLifeFlow(id: UUID) {
        // No-op in demo mode
    }
}