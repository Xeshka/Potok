package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeFlowApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeFlowApi {
    
    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowDTO> {
        // Получаем все сферы жизни
        val lifeAreas = dataSource.getFullLifeAreas()
        
        // Находим нужную сферу жизни
        val lifeArea = lifeAreas.find { it.id == lifeAreaId }
        
        // Возвращаем потоки этой сферы или пустой список
        return lifeArea?.flows ?: emptyList()
    }
    
    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): LifeFlowDTO {
        // В демо-режиме просто возвращаем заглушку
        return LifeFlowDTO(
            id = UUID.randomUUID(),
            areaId = lifeAreaId,
            title = request.title,
            style = request.style ?: "style1",
            placement = request.placement ?: 0,
            status = ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW
        )
    }
    
    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq): LifeFlowDTO {
        // В демо-режиме просто возвращаем заглушку с обновленными данными
        return LifeFlowDTO(
            id = id,
            areaId = UUID.randomUUID(), // В реальном приложении здесь должен быть правильный ID
            title = request.title,
            style = request.style ?: "style1",
            placement = request.placement,
            status = ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW
        )
    }
    
    override suspend fun deleteLifeFlow(id: UUID) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        // Пустая реализация для демо-режима
    }
    
    override suspend fun collocateLifeFlows(lifeAreaId: UUID, flowIds: List<UUID>) {
        // Пустая реализация для демо-режима
    }
}