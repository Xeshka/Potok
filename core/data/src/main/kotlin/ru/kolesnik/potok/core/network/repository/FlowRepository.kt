package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow

interface FlowRepository {
    suspend fun getFlow(flowId: String): LifeFlow?
    fun getFlowByLifeArea(areaId: String): Flow<List<LifeFlow>>
}