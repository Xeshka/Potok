package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.result.Result

interface LifeFlowRepository {
    fun getLifeFlows(): Flow<List<LifeFlow>>
    fun getLifeFlowsByArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun syncLifeFlows(): Result<Unit>
    suspend fun createLifeFlow(name: String, lifeAreaId: String, description: String?): Result<LifeFlow>
    suspend fun updateLifeFlow(id: String, name: String, description: String?): Result<LifeFlow>
    suspend fun deleteLifeFlow(id: String): Result<Unit>
}