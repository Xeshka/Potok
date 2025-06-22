package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import java.util.UUID
import javax.inject.Inject

interface LifeFlowRepository {
    suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID
    suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq)
    suspend fun deleteLifeFlow(id: UUID)
    suspend fun moveLifeFlow(request: LifeFlowMoveDTO)
    suspend fun syncLifeFlows(lifeAreaId: UUID)
    suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowEntity>
}

class LifeFlowRepositoryImpl @Inject constructor(
    private val api: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeFlowRepository {

    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID {
        val response = api.createLifeFlow(lifeAreaId, request)
        val entity = response.toEntity()
        lifeFlowDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq) {
        val updated = api.updateLifeFlow(id, request).toEntity()
        lifeFlowDao.update(updated)
    }

    override suspend fun deleteLifeFlow(id: UUID) {
        api.deleteLifeFlow(id)
        taskDao.deleteByFlowId(id)
        lifeFlowDao.delete(lifeFlowDao.getById(id) ?: return)
    }

    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        TODO("Это будет не просто")
        //api.moveLifeFlow(request)
        // Обновляем позиции в БД
        //request.flowPositions.forEach { (flowId, position) ->
        //    lifeFlowDao.updatePosition(flowId, position)
        //}
    }

    override suspend fun syncLifeFlows(lifeAreaId: UUID) {
        val flows = api.getLifeFlows(lifeAreaId)
        val entities = flows.map { it.toEntity() }
        lifeFlowDao.deleteByAreaId(lifeAreaId)
        lifeFlowDao.insertAll(entities)
    }

    override suspend fun getLifeFlows(lifeAreaId: UUID): List<LifeFlowEntity> {
        return lifeFlowDao.getByAreaId(lifeAreaId)
    }
}

fun LifeFlowDTO.toEntity(): LifeFlowEntity = LifeFlowEntity(
    id = id,
    areaId = areaId,
    title = title,
    style = style,
    placement = placement,
    status = status
)