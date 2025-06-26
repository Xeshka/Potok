package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaRq
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import java.util.UUID
import javax.inject.Inject

class LifeAreaRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao,
    private val syncRepository: SyncRepository
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAllFlow().map { entities ->
            entities.map { entity ->
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
        }
    }

    override suspend fun createLifeArea(title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): String {
        val request = LifeAreaRq(
            title = title,
            style = style,
            tagsId = null,
            placement = null,
            isTheme = isTheme,
            onlyPersonal = onlyPersonal
        )
        
        val response = api.createLifeArea(request)
        syncRepository.syncLifeAreas()
        
        return response.id.toString()
    }

    override suspend fun updateLifeArea(id: String, title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean) {
        val request = LifeAreaRq(
            title = title,
            style = style,
            tagsId = null,
            placement = null,
            isTheme = isTheme,
            onlyPersonal = onlyPersonal
        )
        
        api.updateLifeArea(UUID.fromString(id), request)
        syncRepository.syncLifeAreas()
    }

    override suspend fun deleteLifeArea(id: String) {
        api.deleteLifeArea(UUID.fromString(id))
        
        // Удаляем из локальной базы данных
        val entity = lifeAreaDao.getById(UUID.fromString(id))
        if (entity != null) {
            // Каскадное удаление потоков и задач происходит автоматически благодаря настройкам Room
            lifeAreaDao.delete(entity)
        }
    }
}