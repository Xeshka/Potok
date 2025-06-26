package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.database.entitys.TaskEntity
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.TaskRs
import java.util.UUID
import javax.inject.Inject

interface SyncRepository {
    suspend fun syncAll()
    suspend fun syncLifeAreas()
    suspend fun syncLifeFlows(lifeAreaId: UUID)
    suspend fun syncTasks(flowId: UUID)
}

class SyncRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val db: AppDatabase
) : SyncRepository {

    override suspend fun syncAll() {
        val fullData = api.getFullLifeAreas()
        
        // Очищаем базу данных перед синхронизацией
        db.clearAllTables()
        
        // Сохраняем сферы жизни
        val lifeAreaEntities = fullData.map { it.toEntity() }
        db.lifeAreaDao().insertAll(lifeAreaEntities)
        
        // Сохраняем потоки и задачи для каждой сферы
        fullData.forEach { lifeArea ->
            lifeArea.flows?.let { flows ->
                val flowEntities = flows.map { it.toEntity() }
                db.lifeFlowDao().insertAll(flowEntities)
                
                // Сохраняем задачи для каждого потока
                flows.forEach { flow ->
                    flow.tasks?.let { tasks ->
                        val taskEntities = tasks.map { it.toEntity(flow.id) }
                        db.taskDao().insertAll(taskEntities)
                        
                        // Здесь можно добавить сохранение связанных сущностей (assignees, checklist и т.д.)
                    }
                }
            }
        }
    }

    override suspend fun syncLifeAreas() {
        val lifeAreas = api.getLifeAreas()
        val entities = lifeAreas.map { it.toEntity() }
        db.lifeAreaDao().deleteAll()
        db.lifeAreaDao().insertAll(entities)
    }

    override suspend fun syncLifeFlows(lifeAreaId: UUID) {
        val flows = api.getFullLifeAreas().find { it.id == lifeAreaId }?.flows ?: return
        val entities = flows.map { it.toEntity() }
        db.lifeFlowDao().deleteByAreaId(lifeAreaId)
        db.lifeFlowDao().insertAll(entities)
    }

    override suspend fun syncTasks(flowId: UUID) {
        // Находим сферу жизни, содержащую этот поток
        val fullData = api.getFullLifeAreas()
        val flow = fullData.flatMap { it.flows ?: emptyList() }.find { it.id == flowId } ?: return
        
        val tasks = flow.tasks ?: return
        val entities = tasks.map { it.toEntity(flowId) }
        
        db.taskDao().deleteByFlowId(flowId)
        db.taskDao().insertAll(entities)
    }
    
    // Вспомогательные функции для преобразования DTO в Entity
    private fun LifeAreaDTO.toEntity(): LifeAreaEntity = LifeAreaEntity(
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
    
    private fun LifeFlowDTO.toEntity(): LifeFlowEntity = LifeFlowEntity(
        id = id,
        areaId = areaId,
        title = title,
        style = style,
        placement = placement,
        status = status
    )
    
    private fun TaskRs.toEntity(flowId: UUID): TaskEntity = TaskEntity(
        cardId = cardId,
        externalId = id,
        internalId = internalId,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate,
        payload = payload,
        lifeAreaId = payload.lifeAreaId,
        flowId = flowId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        commentCount = commentCount,
        attachmentCount = attachmentCount
    )
}