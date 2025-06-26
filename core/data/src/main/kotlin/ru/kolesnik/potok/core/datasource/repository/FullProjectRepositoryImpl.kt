package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.extensions.toDomain
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import javax.inject.Inject

class FullProjectRepositoryImpl @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
    private val syncRepository: SyncRepository
) : FullProjectRepository {
    
    override suspend fun sync() {
        syncRepository.syncAll()
    }
    
    override suspend fun getEmployee(employeeNumbers: List<EmployeeId>): List<EmployeeResponse> {
        return syncFullDataSource.getEmployee(employeeNumbers, true)
    }
    
    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}