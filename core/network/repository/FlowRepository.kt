package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import java.util.UUID
import javax.inject.Inject

interface FlowRepository {
    fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun createFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID
    suspend fun updateFlow(id: UUID, request: LifeFlowRq)
    suspend fun deleteFlow(id: UUID)
}

class FlowRepositoryImpl @Inject constructor(
    private val lifeFlowApi: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao
) : FlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> = flow {
        val areaUuid = UUID.fromString(lifeAreaId)
        val flows = lifeFlowDao.getByAreaId(areaUuid).map { entity ->
            LifeFlow(
                id = entity.id.toString(),
                areaId = entity.areaId.toString(),
                title = entity.title,
                style = entity.style,
                placement = entity.placement,
                status = entity.status ?: ru.kolesnik.potok.core.model.FlowStatus.NEW
            )
        }
        emit(flows)
    }

    override suspend fun createFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID {
        val response = lifeFlowApi.createLifeFlow(lifeAreaId, request)
        val entity = response.toEntity()
        lifeFlowDao.insert(entity)
        return entity.id
    }

    override suspend fun updateFlow(id: UUID, request: LifeFlowRq) {
        val updated = lifeFlowApi.updateLifeFlow(id, request)
        lifeFlowDao.update(updated.toEntity())
    }

    override suspend fun deleteFlow(id: UUID) {
        lifeFlowApi.deleteLifeFlow(id)
        lifeFlowDao.delete(lifeFlowDao.getById(id) ?: return)
    }
}