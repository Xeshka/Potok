package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.FlowRepository
import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.SyncFullDataSource
import java.util.UUID
import javax.inject.Inject

internal class FlowRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val lifeFlowDao: LifeFlowDao
) : FlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        return lifeFlowDao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncFlowsForArea(lifeAreaId: String) {
        try {
            val networkData = syncDataSource.getFull()
            val areaUuid = UUID.fromString(lifeAreaId)
            
            // Find flows for this area
            val flows = networkData
                .find { it.id == lifeAreaId }
                ?.flows
                ?.map { it.toEntity() }
                ?: emptyList()
            
            // Clear existing flows for this area and insert new ones
            lifeFlowDao.deleteByAreaId(areaUuid)
            lifeFlowDao.insertAll(flows)
        } catch (e: Exception) {
            println("Failed to sync flows for area $lifeAreaId: ${e.message}")
        }
    }

    override suspend fun getFlowById(flowId: String): LifeFlow? {
        return lifeFlowDao.getById(UUID.fromString(flowId))?.toDomain()
    }
}