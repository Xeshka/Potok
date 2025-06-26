package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.data.repository.FullProjectRepository
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.model.extensions.toEntity
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import javax.inject.Inject

class FullProjectRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val database: AppDatabase
) : FullProjectRepository {

    override suspend fun sync() {
        try {
            val fullData = syncDataSource.gtFullNew()
            
            // Очищаем базу данных
            database.clearAllTables()
            
            // Сохраняем данные в базу
            fullData.forEach { lifeAreaDto ->
                val lifeAreaEntity = lifeAreaDto.toEntity()
                database.lifeAreaDao().insert(lifeAreaEntity)
                
                lifeAreaDto.flows?.forEach { flowDto ->
                    val flowEntity = flowDto.toEntity()
                    database.lifeFlowDao().insert(flowEntity)
                    
                    flowDto.tasks?.forEach { taskDto ->
                        val taskEntity = taskDto.toEntity()
                        database.taskDao().insert(taskEntity)
                        
                        taskDto.assignees?.forEach { assigneeDto ->
                            val assigneeEntity = assigneeDto.toEntity(taskEntity.cardId)
                            database.taskAssigneeDao().insert(assigneeEntity)
                        }
                        
                        taskDto.checkList?.forEach { checklistDto ->
                            val checklistEntity = checklistDto.toEntity(taskEntity.cardId)
                            database.checklistTaskDao().insert(checklistEntity)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw Exception("Ошибка синхронизации: ${e.message}", e)
        }
    }

    override suspend fun getEmployee(employeeIds: List<String>): List<EmployeeResponse> {
        return syncDataSource.getEmployee(employeeIds, avatar = true)
    }
}