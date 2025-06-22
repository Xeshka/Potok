package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import java.util.UUID
import javax.inject.Inject

interface LifeAreaRepository {
    suspend fun createLifeArea(request: LifeAreaRq): UUID
    suspend fun updateLifeArea(id: UUID, request: LifeAreaRq)
    suspend fun deleteLifeArea(id: UUID)
    suspend fun syncLifeAreas()
    suspend fun getLifeAreas(): List<LifeAreaEntity>
}

class LifeAreaRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeAreaRepository {

    override suspend fun createLifeArea(request: LifeAreaRq): UUID {
        val response = api.createLifeArea(request)
        val entity = response.toEntity()
        lifeAreaDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) {
        val updated = api.updateLifeArea(id, request)
        lifeAreaDao.update(updated.toEntity())
    }

    override suspend fun deleteLifeArea(id: UUID) {
        api.deleteLifeArea(id)
        lifeFlowDao.deleteByAreaId(id)
        taskDao.deleteByAreaId(id)
        lifeAreaDao.delete(lifeAreaDao.getById(id) ?: return)
    }

    override suspend fun syncLifeAreas() {
        val areas = api.getFullLifeAreas()
        val entities = areas.map { it.toEntity() }
        lifeAreaDao.deleteAll()
        lifeAreaDao.insertAll(entities)
    }

    override suspend fun getLifeAreas(): List<LifeAreaEntity> {
        return lifeAreaDao.getAll()
    }
}

// Маппер из DTO в Entity
fun LifeAreaDTO.toEntity(): LifeAreaEntity = LifeAreaEntity(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    sharedInfo = sharedInfo,
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)