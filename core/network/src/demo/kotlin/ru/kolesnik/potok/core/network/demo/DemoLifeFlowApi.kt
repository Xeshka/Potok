package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeFlowApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeFlowApi {
    
    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        // Заглушка для демо-режима
    }
    
    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowDTO> {
        return dataSource.gtFullNew()
            .find { it.id == lifeAreaId }
            ?.flows ?: emptyList()
    }
    
    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Заглушка для демо-режима
        return dataSource.gtFullNew()
            .find { it.id == lifeAreaId }
            ?.flows?.firstOrNull() ?: throw IllegalStateException("No flows found")
    }
    
    override suspend fun collocateLifeFlows(lifeAreaId: UUID, flowIds: List<UUID>) {
        // Заглушка для демо-режима
    }
    
    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Заглушка для демо-режима
        return dataSource.gtFullNew()
            .flatMap { it.flows ?: emptyList() }
            .find { it.id == id } ?: throw IllegalStateException("Flow not found")
    }
    
    override suspend fun deleteLifeFlow(id: UUID) {
        // Заглушка для демо-режима
    }
}