package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import java.util.UUID
import javax.inject.Inject

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(request: LifeAreaRq): UUID
    suspend fun updateLifeArea(id: UUID, request: LifeAreaRq)
    suspend fun deleteLifeArea(id: UUID)
}

class LifeAreaRepositoryImpl @Inject constructor(
    private val lifeAreaApi: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> = flow {
        val areas = lifeAreaDao.getAll().map { entity ->
            LifeArea(
                id = entity.id,
                title = entity.title,
                style = entity.style,
                tagsId = entity.tagsId,
                placement = entity.placement,
                isDefault = entity.isDefault,
                isTheme = entity.isTheme,
                shared = entity.sharedInfo
            )
        }
        emit(areas)
    }

    override suspend fun createLifeArea(request: LifeAreaRq): UUID {
        val response = lifeAreaApi.createLifeArea(request)
        val entity = response.toEntity()
        lifeAreaDao.insert(entity)
        return entity.id
    }

    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) {
        val updated = lifeAreaApi.updateLifeArea(id, request)
        lifeAreaDao.update(updated.toEntity())
    }

    override suspend fun deleteLifeArea(id: UUID) {
        lifeAreaApi.deleteLifeArea(id)
        lifeAreaDao.delete(lifeAreaDao.getById(id) ?: return)
    }
}