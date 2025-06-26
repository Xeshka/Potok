package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.Employee
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse

interface FullProjectRepository {
    suspend fun sync()
    suspend fun getEmployee(employeeNumbers: List<EmployeeId>): List<EmployeeResponse>
    fun getEmployees(): Flow<List<Employee>>
}