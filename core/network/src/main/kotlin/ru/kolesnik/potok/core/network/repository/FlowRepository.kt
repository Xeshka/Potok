package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow
import java.util.UUID

interface FlowRepository {
    fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun createFlow(lifeAreaId: UUID, title: String, style: String?): UUID
    suspend fun updateFlow(flowId: UUID, title: String, style: String?): LifeFlow
    suspend fun deleteFlow(flowId: UUID)
}