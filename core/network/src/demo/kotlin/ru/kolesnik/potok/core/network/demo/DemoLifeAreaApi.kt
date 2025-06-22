package ru.kolesnik.potok.core.network.demo

import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.retrofit.LifeAreaApi
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoLifeAreaApi @Inject constructor(
    private val dataSource: DemoSyncFullDataSource
) : LifeAreaApi {
    
    override suspend fun getLifeAreas(): List<LifeAreaDTO> {
        return dataSource.getFullLifeAreas()
    }
    
    override suspend fun createLifeArea(request: LifeAreaRq): LifeAreaDTO {
        // Заглушка для демо-режима
        return dataSource.getFullLifeAreas().first()
    }
    
    override suspend fun collocateLifeAreas(lifeAreaIds: List<UUID>) {
        // Пустая реализация для демо
    }
    
    override suspend fun moveLifeArea(request: LifeAreaMoveDTO) {
        // Пустая реализация для демо
    }
    
    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq): LifeAreaDTO {
        // Заглушка для демо-режима
        return dataSource.getFullLifeAreas().first()
    }
    
    override suspend fun deleteLifeArea(id: UUID) {
        // Пустая реализация для демо
    }
}