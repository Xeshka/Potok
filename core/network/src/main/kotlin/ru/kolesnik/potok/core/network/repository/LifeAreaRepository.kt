package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(lifeArea: LifeArea): LifeArea
    suspend fun updateLifeArea(lifeArea: LifeArea): LifeArea
    suspend fun deleteLifeArea(id: String)
    suspend fun syncLifeAreas()
}