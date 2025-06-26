package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.data.repository.LifeAreaRepository
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.model.extensions.toEntity
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import java.util.UUID
import javax.inject.Inject

class LifeAreaRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createLifeArea(
        title: String,
        style: String?,
        isTheme: Boolean,
        onlyPersonal: Boolean
    ): UUID {
        val request = LifeAreaRq(
            title = title,
            style = style,
            isTheme = isTheme,
            onlyPersonal = onlyPersonal
        )
        val response = api.createLifeArea(request)
        val entity = response.toEntity()
        lifeAreaDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeArea(id: UUID, title: String, style: String?) {
        val request = LifeAreaRq(
            title = title,
            style = style,
            isTheme = false,
            onlyPersonal = false
        )
        val updated = api.updateLifeArea(id, request)
        lifeAreaDao.update(updated.toEntity())
    }

    override suspend fun deleteLifeArea(id: UUID) {
        api.deleteLifeArea(id)
        lifeFlowDao.deleteByAreaId(id)
        taskDao.deleteByAreaId(id)
        lifeAreaDao.getById(id)?.let { lifeAreaDao.delete(it) }
    }

    override suspend fun syncLifeAreas() {
        val areas = api.getFullLifeAreas()
        val entities = areas.map { it.toEntity() }
        lifeAreaDao.deleteAll()
        lifeAreaDao.insertAll(entities)
    }
}