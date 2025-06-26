package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.network.result.Result
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.database.dao.TaskAssigneeDao
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitSyncFullDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepositoryImpl @Inject constructor(
    private val syncFullDataSource: RetrofitSyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
    private val lifeFlowDao: LifeFlowDao,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao
) : SyncRepository {

    override suspend fun syncAll(): Result<Unit> {
        return try {
            val fullData = syncFullDataSource.gtFullNew()
            
            // Синхронизируем области жизни
            val lifeAreaEntities = fullData.map { it.toEntity() }
            lifeAreaDao.insertAll(lifeAreaEntities)
            
            // Синхронизируем потоки
            fullData.forEach { lifeArea ->
                lifeArea.flows?.let { flows ->
                    val flowEntities = flows.map { it.toEntity() }
                    lifeFlowDao.insertAll(flowEntities)
                    
                    // Синхронизируем задачи
                    flows.forEach { flow ->
                        flow.tasks?.let { tasks ->
                            val taskEntities = tasks.map { it.toEntity() }
                            taskDao.insertAll(taskEntities)
                            
                            // Синхронизируем назначенных
                            tasks.forEach { task ->
                                task.assignees?.forEach { assignee ->
                                    val assigneeEntity = ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity(
                                        taskCardId = task.cardId,
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