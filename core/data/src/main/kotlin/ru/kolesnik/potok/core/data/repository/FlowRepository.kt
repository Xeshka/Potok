package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeFlow
import java.util.UUID

interface FlowRepository {
    fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun createFlow(lifeAreaId: UUID, title: String, style: String): UUID
    suspend fun updateFlow(id: UUID, title: String, style: String)
    suspend fun deleteFlow(id: UUID)
    suspend fun syncFlows(lifeAreaId: UUID)
}