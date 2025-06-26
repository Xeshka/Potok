package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.result.Result

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun syncLifeAreas(): Result<Unit>
    suspend fun createLifeArea(name: String, description: String?): Result<LifeArea>
    suspend fun updateLifeArea(id: String, name: String, description: String?): Result<LifeArea>
    suspend fun deleteLifeArea(id: String): Result<Unit>
}