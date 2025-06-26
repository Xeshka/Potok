package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.data.util.toDomain
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FullProjectRepositoryImpl @Inject constructor(
    private val syncDataSource: SyncFullDataSource,
    private val lifeAreaRepository: LifeAreaRepository,
    private val flowRepository: FlowRepository
) : FullProjectRepository {

    override suspend fun sync() {
        try {
            // Sync life areas first
            lifeAreaRepository.syncLifeAreas()
            
            // Then sync flows for each area
            // This is a simplified approach - in real app you might want to optimize this
            val areas = syncDataSource.gtFullNew()
            areas.forEach { area ->
                flowRepository.syncFlowsForArea(area.id.toString())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getEmployee(employeeIds: List<String>): List<Employee> {
        return try {
            val employees = syncDataSource.getEmployee(employeeIds, avatar = true)
            employees.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}