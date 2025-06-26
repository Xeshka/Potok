package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.data.util.toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao
) : SyncRepository {

    override suspend fun syncAll(): Result<Unit> {
        return try {
            // ✅ Получаем DTO из сетевого слоя
            val lifeAreaDTOs = syncFullDataSource.gtFullNew()
            
            // ✅ Преобразуем DTO в доменные модели в слое данных
            val lifeAreas = lifeAreaDTOs.map { dto ->
                dto.toDomainModel()
            }
            
            // ✅ Преобразуем доменные модели в Entity для базы данных
            val lifeAreaEntities = lifeAreas.map { it.toEntity() }
            lifeAreaDao.insertAll(lifeAreaEntities)
            
            // ✅ Синхронизируем потоки
            lifeAreaDTOs.forEach { lifeAreaDTO ->
                lifeAreaDTO.flows?.let { flowDTOs ->
                    val flows = flowDTOs.map { it.toDomainModel() }
                    val flowEntities = flows.map { it.toEntity() }
                    lifeFlowDao.insertAll(flowEntities)
                    
                    // ✅ Синхронизируем задачи
                    flowDTOs.forEach { flowDTO ->
                        flowDTO.tasks?.let { taskDTOs ->
                            val tasks = taskDTOs.map { it.toDomainModel() }
                            val taskEntities = tasks.map { it.toEntity() }
                            taskDao.insertAll(taskEntities)
                            
                            // ✅ Синхронизируем назначенных
                            taskDTOs.forEach { taskDTO ->
                                taskDTO.assignees?.forEach { assigneeDTO ->
                                    val assignee = assigneeDTO.toDomainModel()
                                    val assigneeEntity = ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity(
                                        taskCardId = taskDTO.cardId,
                                        employeeId = assignee.employeeId,
                                        complete = assignee.complete
                                    )
                                    taskAssigneeDao.insert(assigneeEntity)
                                }
                            }
                        }
                    }
                }
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun syncFullProject(): Result<Unit> {
        return syncAll()
    }
}