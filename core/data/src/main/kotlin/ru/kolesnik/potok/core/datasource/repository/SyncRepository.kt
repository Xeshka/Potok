package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import javax.inject.Inject

interface SyncRepository {
    suspend fun sync()
}

class SyncRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val db: AppDatabase
) : SyncRepository {
    
    override suspend fun sync() {
        // Получаем все сферы жизни с сервера
        val lifeAreas = api.getFullLifeAreas()
        
        // Преобразуем в сущности базы данных
        val lifeAreaEntities = lifeAreas.map { dto ->
            LifeAreaEntity(
                id = dto.id,
                title = dto.title,
                style = dto.style,
                tagsId = dto.tagsId,
                placement = dto.placement,
                isDefault = dto.isDefault,
                sharedInfo = dto.sharedInfo,
                isTheme = dto.isTheme,
                onlyPersonal = dto.onlyPersonal
            )
        }
        
        // Собираем все потоки из всех сфер
        val lifeFlowEntities = lifeAreas.flatMap { area ->
            area.flows?.map { flow ->
                LifeFlowEntity(
                    id = flow.id,
                    areaId = flow.areaId,
                    title = flow.title,
                    style = flow.style,
                    placement = flow.placement,
                    status = flow.status
                )
            } ?: emptyList()
        }
        
        // Собираем все задачи из всех потоков
        val taskEntities = lifeAreas.flatMap { area ->
            area.flows?.flatMap { flow ->
                flow.tasks?.map { task ->
                    task.toEntity()
                } ?: emptyList()
            } ?: emptyList()
        }
        
        // Транзакция для обновления базы данных
        db.runInTransaction {
            // Очищаем старые данные
            db.lifeAreaDao().deleteAll()
            
            // Вставляем новые данные
            db.lifeAreaDao().insertAll(lifeAreaEntities)
            db.lifeFlowDao().insertAll(lifeFlowEntities)
            db.taskDao().insertAll(taskEntities)
            
            // Обрабатываем связанные сущности для задач
            taskEntities.forEach { task ->
                // Сохраняем исполнителей задачи
                task.assignees?.let { assignees ->
                    db.taskAssigneeDao().insertAll(assignees)
                }
                
                // Сохраняем чек-листы задачи
                task.checkList?.let { checkItems ->
                    db.checklistTaskDao().insertAll(checkItems)
                }
            }
        }
    }
    
    private fun ru.kolesnik.potok.core.network.model.api.TaskRs.toEntity(): ru.kolesnik.potok.core.database.entitys.TaskEntity {
        return ru.kolesnik.potok.core.database.entitys.TaskEntity(
            cardId = cardId,
            externalId = id,
            internalId = internalId,
            title = title,
            subtitle = subtitle,
            mainOrder = mainOrder,
            source = source,
            taskOwner = taskOwner,
            creationDate = creationDate,
            payload = payload.toDomain(),
            lifeAreaId = payload.lifeAreaId,
            flowId = flowId?.let { java.util.UUID.fromString(it.toString()) },
            lifeAreaPlacement = lifeAreaPlacement,
            flowPlacement = flowPlacement,
            commentCount = commentCount,
            attachmentCount = attachmentCount,
            deletedAt = null
        )
    }
}