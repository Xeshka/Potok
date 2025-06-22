package ru.kolesnik.potok.core.datasource.repository

import androidx.room.withTransaction
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.api.*

import javax.inject.Inject

interface SyncRepository {
    suspend fun fullSync()
}

class SyncRepositoryImpl @Inject constructor(
    private val api: LifeAreaApi,
    private val db: AppDatabase
) : SyncRepository {

    override suspend fun fullSync() {
        // Получаем полные данные с сервера
        val fullData = api.getFullLifeAreas()
        
        // Преобразуем DTO в Entity
        val (areas, flows, tasks) = mapFullData(fullData)
        
        // Сохраняем в БД в единой транзакции
        db.withTransaction  {
            // Очищаем текущие данные
            db.lifeAreaDao().deleteAll()
            db.lifeFlowDao().deleteAll()
            db.taskDao().deleteAll()
            
            // Сохраняем новые данные
            db.lifeAreaDao().insertAll(areas)
            db.lifeFlowDao().insertAll(flows)
            db.taskDao().insertAll(tasks)
        }
    }

    private fun mapFullData(fullData: List<LifeAreaDTO>):
        Triple<List<LifeAreaEntity>, List<LifeFlowEntity>, List<TaskEntity>> {
        
        val areas = mutableListOf<LifeAreaEntity>()
        val flows = mutableListOf<LifeFlowEntity>()
        val tasks = mutableListOf<TaskEntity>()
        
        fullData.forEach { areaDto ->
            // Маппинг области
            val areaEntity = areaDto.toEntity()
            areas.add(areaEntity)
            
            // Маппинг потоков области
            areaDto.flows?.forEach { flowDto ->
                val flowEntity = flowDto.toEntity(areaEntity.id)
                flows.add(flowEntity)
                
                // Маппинг задач потока
                flowDto.tasks?.forEach { taskDto ->
                    val taskEntity = taskDto.toEntity(flowEntity.id, areaEntity.id)
                    tasks.add(taskEntity)
                }
            }
        }
        
        return Triple(areas, flows, tasks)
    }
}
