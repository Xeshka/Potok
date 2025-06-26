package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.datasource.LifeFlowDataSource
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject

class RetrofitLifeFlowDataSource @Inject constructor(
    private val api: LifeFlowApi
) : LifeFlowDataSource {
    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) = api.moveLifeFlow(request)
    override suspend fun getLifeFlows(lifeAreaId: UUID) = api.getLifeFlows(lifeAreaId)
    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq) = api.createLifeFlow(lifeAreaId, request)
    override suspend fun collocateLifeFlows(lifeAreaId: UUID, flowIds: List<UUID>) = api.collocateLifeFlows(lifeAreaId, flowIds)
    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq) = api.updateLifeFlow(id, request)
    override suspend fun deleteLifeFlow(id: UUID) = api.deleteLifeFlow(id)
}