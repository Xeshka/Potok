package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface LifeFlowDataSource {
    suspend fun moveLifeFlow(request: LifeFlowMoveDTO)
    suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowDTO>
    suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): LifeFlowDTO
    suspend fun collocateLifeFlows(lifeAreaId: UUID, flowIds: List<UUID>)
    suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq): LifeFlowDTO
    suspend fun deleteLifeFlow(id: UUID)
}