package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import java.util.UUID
import javax.inject.Inject

class LifeAreaRepositoryImpl @Inject constructor(
    private val lifeAreaApi: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createLifeArea(title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): UUID {
        val request = LifeAreaRq(
            title = title,
            style = style,
            tagsId = null,
            placement = null,
            isTheme = isTheme,
            onlyPersonal = onlyPersonal
        )
        
        val response = lifeAreaApi.createLifeArea(request)
        val entity = LifeAreaEntity(
            id = response.id,
            title = response.title,
            style = response.style,
            tagsId = response.tagsId,
            placement = response.placement,
            isDefault = response.isDefault,
            sharedInfo = response.sharedInfo,
            isTheme = response.isTheme,
            onlyPersonal = response.onlyPersonal
        )
        
        lifeAreaDao.insert(entity)
        return response.id
    }

    override suspend fun updateLifeArea(id: UUID, title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): LifeArea {
        val request = LifeAreaRq(
            title = title,
            style = style,
            tagsId = null,
            placement = null,
            isTheme = isTheme,
            onlyPersonal = onlyPersonal
        )
        
        val response = lifeAreaApi.updateLifeArea(id, request)
        val entity = LifeAreaEntity(
            id = response.id,
            title = response.title,
            style = response.style,
            tagsId = response.tagsId,
            placement = response.placement,
            isDefault = response.isDefault,
            sharedInfo = response.sharedInfo,
            isTheme = response.isTheme,
            onlyPersonal = response.onlyPersonal
        )
        
        lifeAreaDao.update(entity)
        return entity.toDomain()
    }

    override suspend fun deleteLifeArea(id: UUID) {
        lifeAreaApi.deleteLifeArea(id)
        val entity = lifeAreaDao.getById(id)
        if (entity != null) {
            lifeAreaDao.delete(entity)
        }
    }
    
    private fun LifeAreaEntity.toDomain(): LifeArea = LifeArea(
        id = id,
        title = title,
        style = style,
        tagsId = tagsId,
        placement = placement,
        isDefault = isDefault,
        isTheme = isTheme,
        shared = sharedInfo?.let {
            LifeAreaSharedInfo(
                areaId = id,
                owner = it.owner,
                recipients = it.recipients
            )
        }
    )
}