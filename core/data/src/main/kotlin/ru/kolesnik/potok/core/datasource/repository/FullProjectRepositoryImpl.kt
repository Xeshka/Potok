package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.data.repository.FullProjectRepository
import ru.kolesnik.potok.core.data.util.toEntity
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import java.util.UUID
import javax.inject.Inject

internal class FullProjectRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val database: AppDatabase
) : FullProjectRepository {

    override suspend fun sync() {
        try {
            val networkData = syncDataSource.getFull()
            
            // Clear all data
            database.clearAllTables()
            
            // Sync life areas
            val lifeAreaEntities = networkData.map { it.toEntity() }
            database.lifeAreaDao().insertAll(lifeAreaEntities)
            
            // Sync flows and tasks for each area
            networkData.forEach { networkArea ->
                val areaId = UUID.fromString(networkArea.id)
                
                // Sync flows
                networkArea.flows?.let { flows ->
                    val flowEntities = flows.map { it.toEntity() }
                    database.lifeFlowDao().insertAll(flowEntities)
                    
                    // Sync tasks for each flow
                    flows.forEach { flow ->
                        val flowId = UUID.fromString(flow.id)
                        flow.tasks?.let { tasks ->
                            val taskEntities = tasks.map { task ->
                                task.toEntity().copy(
                                    lifeAreaId = areaId,
                                    flowId = flowId
                                )
                            }
                            database.taskDao().insertAll(taskEntities)
                            
                            // Sync assignees and checklist for each task
                            tasks.forEach { networkTask ->
                                val taskId = UUID.fromString(networkTask.id)
                                
                                // Sync assignees
                                networkTask.assignees?.let { assignees ->
                                    val assigneeEntities = assignees.map { it.toEntity(taskId) }
                                    database.taskAssigneeDao().insertAll(assigneeEntities)
                                }
                                
                                // Sync checklist
                                networkTask.checkList?.let { checklist ->
                                    val checklistEntities = checklist.map { it.toEntity(taskId) }
                                    database.checklistTaskDao().insertAll(checklistEntities)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw Exception("Sync failed: ${e.message}", e)
        }
    }

    override suspend fun getEmployee(employeeIds: List<String>): List<EmployeeResponse> {
        return syncDataSource.getEmployee(employeeIds, avatar = true)
    }
}