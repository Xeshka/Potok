package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow

interface FlowRepository {
    fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun createFlow(lifeAreaId: String, title: String, style: String): String
    suspend fun updateFlow(flowId: String, title: String, style: String)
    suspend fun deleteFlow(flowId: String)
}