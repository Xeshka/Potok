package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.potok.toFlowDomainModel
import javax.inject.Inject

class DefaultFlowRepository @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeFlowDao: LifeFlowDao,
) : FlowRepository {

    override suspend fun getFlow(flowId: String): LifeFlow? {
        return lifeFlowDao.getFlowById(flowId)?.toFlowDomainModel()
    }

    override fun getFlowByLifeArea(areaId: String): Flow<List<LifeFlow>> {
        return lifeFlowDao.getFlowsForArea(areaId).map { it.toFlowDomainModel() }
    }

}