package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

interface LifeAreaDataSource {
    suspend fun getLifeAreas(): List<LifeAreaDTO>
    suspend fun createLifeArea(request: LifeAreaRq): LifeAreaDTO
    suspend fun collocateLifeAreas(lifeAreaIds: List<UUID>)
    suspend fun getFullLifeAreas(): List<LifeAreaDTO>
    suspend fun moveLifeArea(request: LifeAreaMoveDTO)
    suspend fun updateLifeArea(id: UUID, request: LifeAreaRq): LifeAreaDTO
    suspend fun deleteLifeArea(id: UUID)
}