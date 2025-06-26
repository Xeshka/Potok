package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeAreaMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.network.retrofit.LifeAreaApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeAreaApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeAreaApi {
    
    override suspend fun getLifeAreas(): List<LifeAreaDTO> {
        return dataSource.gtFullNew()
    }
    
    override suspend fun getFullLifeAreas(): List<LifeAreaDTO> {
        return dataSource.gtFullNew()
    }
    
    override suspend fun createLifeArea(request: LifeAreaRq): LifeAreaDTO {
        // Создаем заглушечную сферу жизни
        return LifeAreaDTO(
            id = UUID.randomUUID(),
            title = request.title,
            style = request.style,
            tagsId = request.tagsId,
            placement = request.placement,
            isDefault = false,
            isTheme = request.isTheme,
            onlyPersonal = request.onlyPersonal
        )
    }
    
    override suspend fun collocateLifeAreas(lifeAreaIds: List<UUID>) {
        // Пустая реализация для демо
    }
    
    override suspend fun moveLifeArea(request: LifeAreaMoveDTO) {
        // Пустая реализация для демо
    }
    
    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq): LifeAreaDTO {
        // Возвращаем обновленную сферу жизни
        return LifeAreaDTO(
            id = id,
            title = request.title,
            style = request.style,
            tagsId = request.tagsId,
            placement = request.placement,
            isDefault = false,
            isTheme = request.isTheme,
            onlyPersonal = request.onlyPersonal
        )
    }
    
    override suspend fun deleteLifeArea(id: UUID) {
        // Пустая реализация для демо
    }
}