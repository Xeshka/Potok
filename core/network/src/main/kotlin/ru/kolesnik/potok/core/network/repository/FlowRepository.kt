package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow

interface FlowRepository {
    fun getFlowByLifeArea(areaId: String): Flow<List<LifeFlow>>
    suspend fun createFlow(flow: LifeFlow): LifeFlow
    suspend fun updateFlow(flow: LifeFlow): LifeFlow
    suspend fun deleteFlow(id: String)
}