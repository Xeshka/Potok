package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import javax.inject.Inject

interface SyncRepository {
    suspend fun syncAll()
}

class SyncRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val db: AppDatabase
) : SyncRepository {
    
    override suspend fun syncAll() {
        try {
            // Get all life areas with flows and tasks
            val lifeAreas = api.getFullLifeAreas()
            
            // Use a transaction to ensure data consistency
            db.runInTransaction {
                // Clear existing data
                db.lifeAreaDao().deleteAll()
                db.lifeFlowDao().deleteAll()
                db.taskDao().deleteAll()
                db.checklistTaskDao().deleteAll()
                db.taskAssigneeDao().deleteAll()
                db.taskCommentDao().deleteAll()
                
                // Insert life areas
                val lifeAreaEntities = lifeAreas.map { it.toEntity() }
                db.lifeAreaDao().insertAll(lifeAreaEntities)
                
                // Insert flows for each life area
                lifeAreas.forEach { lifeArea ->
                    lifeArea.flows?.let { flows ->
                        val flowEntities = flows.map { it.toEntity() }
                        db.lifeFlowDao().insertAll(flowEntities)
                        
                        // Insert tasks for each flow
                        flows.forEach { flow ->
                            flow.tasks?.let { tasks ->
                                val taskEntities = tasks.map { it.toEntity() }
                                db.taskDao().insertAll(taskEntities)
                                
                                // Insert assignees for each task
                                tasks.forEach { task ->
                                    task.assignees?.let { assignees ->
                                        val assigneeEntities = assignees.map { 
                                            it.toEntity(task.cardId) 
                                        }
                                        db.taskAssigneeDao().insertAll(assigneeEntities)
                                    }
                                    
                                    // Insert checklist items for each task
                                    task.checkList?.let { checklistItems ->
                                        val checklistEntities = checklistItems.map { 
                                            it.toChecklistEntity(task.cardId) 
                                        }
                                        db.checklistTaskDao().insertAll(checklistEntities)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Log error but don't crash
            e.printStackTrace()
            throw e
        }
    }
}