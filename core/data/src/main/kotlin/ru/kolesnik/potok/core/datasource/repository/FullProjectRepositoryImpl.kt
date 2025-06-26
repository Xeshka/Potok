package ru.kolesnik.potok.core.datasource.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.employee.toDomain
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import javax.inject.Inject

class FullProjectRepositoryImpl @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource
) : FullProjectRepository {
    
    private val employeeCache = mutableMapOf<String, EmployeeResponse>()
    
    override suspend fun sync() {
        // Получаем полные данные с сервера
        val fullData = syncFullDataSource.getFull()
        
        // Здесь можно добавить логику для сохранения данных в локальную базу
        // Например, преобразовать NetworkLifeArea в LifeAreaEntity и сохранить
    }
    
    override suspend fun getEmployee(employeeNumbers: List<EmployeeId>): List<EmployeeResponse> {
        // Проверяем кэш
        val cachedEmployees = employeeNumbers.mapNotNull { employeeCache[it] }
        
        // Если все сотрудники найдены в кэше, возвращаем их
        if (cachedEmployees.size == employeeNumbers.size) {
            return cachedEmployees
        }
        
        // Иначе запрашиваем с сервера
        val missingEmployeeIds = employeeNumbers.filter { !employeeCache.containsKey(it) }
        val fetchedEmployees = syncFullDataSource.getEmployee(missingEmployeeIds, true)
        
        // Обновляем кэш
        fetchedEmployees.forEach { employeeCache[it.employeeId] = it }
        
        // Возвращаем все запрошенные сотрудники
        return employeeNumbers.mapNotNull { employeeId ->
            employeeCache[employeeId]
        }
    }
    
    override fun getEmployees(): Flow<List<Employee>> = flow {
        // Здесь можно добавить логику для получения сотрудников из локальной базы
        // Пока просто возвращаем кэшированных сотрудников
        emit(employeeCache.values.map { it.toDomain() })
    }
}