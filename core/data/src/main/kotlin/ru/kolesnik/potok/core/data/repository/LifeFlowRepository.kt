package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.model.api.LifeFlowMoveDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowRq
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toDomain
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface LifeFlowRepository {
    fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>>
    suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID
    suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq)
    suspend fun deleteLifeFlow(id: UUID)
    suspend fun moveLifeFlow(request: LifeFlowMoveDTO)
    suspend fun syncLifeFlows(lifeAreaId: UUID)
}

@Singleton
class LifeFlowRepositoryImpl @Inject constructor(
    private val api: LifeFlowApi,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeFlowRepository {

    override fun getFlowByLifeArea(lifeAreaId: String): Flow<List<LifeFlow>> {
        return lifeFlowDao.getByAreaId(UUID.fromString(lifeAreaId)).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createLifeFlow(lifeAreaId: UUID, request: LifeFlowRq): UUID {
        val response = api.createLifeFlow(lifeAreaId, request)
        val entity = response.toEntity()
        lifeFlowDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeFlow(id: UUID, request: LifeFlowRq) {
        val updated = api.updateLifeFlow(id, request)
        lifeFlowDao.update(updated.toEntity())
    }

    override suspend fun deleteLifeFlow(id: UUID) {
        api.deleteLifeFlow(id)
        // Каскадное удаление связанных задач
        taskDao.deleteByFlowId(id)
        lifeFlowDao.getById(id)?.let { lifeFlowDao.delete(it) }
    }

    override suspend fun moveLifeFlow(request: LifeFlowMoveDTO) {
        api.moveLifeFlow(request)
        // Обновляем позицию в БД
        lifeFlowDao.updatePosition(request.flowId, request.placement)
    }

    override suspend fun syncLifeFlows(lifeAreaId: UUID) {
        val flows = api.getLifeFlows(lifeAreaId)
        val entities = flows.map { it.toEntity() }
        lifeFlowDao.deleteByAreaId(lifeAreaId)
        lifeFlowDao.insertAll(entities)
    }
}