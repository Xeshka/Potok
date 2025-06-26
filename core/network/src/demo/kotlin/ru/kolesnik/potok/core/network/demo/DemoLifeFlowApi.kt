package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.FlowStatus
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.network.retrofit.LifeFlowApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeFlowApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeFlowApi {
    
    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        // Пустая реализация для демо
    }
    
    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowDTO> {
        // Получаем все сферы жизни
        val areas = dataSource.gtFullNew()
        
        // Находим нужную сферу по ID
        val area = areas.find { it.id == lifeAreaId }
        
        // Возвращаем потоки для этой сферы или пустой список
        return area?.flows ?: emptyList()
    }
    
    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Создаем заглушечный поток
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
        // Пустая реализация для демо
    }
    
    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq): LifeFlowDTO {
        // Возвращаем обновленный поток
        return LifeFlowDTO(
            id = id,
            areaId = UUID.randomUUID(), // В реальном приложении здесь должен быть правильный ID сферы
            title = request.title,
            style = request.style ?: "style1",
            placement = request.placement,
            status = FlowStatus.NEW
        )
    }
    
    override suspend fun deleteLifeFlow(id: UUID) {
        // Пустая реализация для демо
    }
}