package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import javax.inject.Inject

interface SyncRepository {
    suspend fun sync()
}

class SyncRepositoryImpl @Inject constructor(
    private val lifeAreaApi: LifeAreaApi,
    private val db: AppDatabase
) : SyncRepository {

    override suspend fun sync() {
        // Получаем все данные с сервера
        val areas = lifeAreaApi.getFullLifeAreas()
        
        // Очищаем базу данных перед синхронизацией
        db.clearAllTables()
        
        // Сохраняем сферы жизни
        val lifeAreaEntities = areas.map { it.toEntity() }
        db.lifeAreaDao().insertAll(lifeAreaEntities)
        
        // Сохраняем потоки для каждой сферы
        areas.forEach { area ->
            area.flows?.let { flows ->
                val flowEntities = flows.map { it.toEntity() }
                db.lifeFlowDao().insertAll(flowEntities)
                
                // Сохраняем задачи для каждого потока
                flows.forEach { flow ->
                    flow.tasks?.let { tasks ->
                        tasks.forEach { task ->
                            // Сохраняем основную информацию о задаче
                            val taskEntity = task.toEntity()
                            db.taskDao().insert(taskEntity)
                            
                            // Сохраняем ответственных за задачу
                            task.assignees?.let { assignees ->
                                val assigneeEntities = assignees.map { 
                                    it.toEntity(taskEntity.cardId)
                                }
                                db.taskAssigneeDao().insertAll(assigneeEntities)
                            }
                            
                            // Сохраняем чек-лист задачи
                            task.checkList?.let { checkList ->
                                val checkListEntities = checkList.map { 
                                    it.toChecklistEntity(taskEntity.cardId)
                                }
                                db.checklistTaskDao().insertAll(checkListEntities)
                            }
                        }
                    }
                }
            }
        }
    }
}