package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse

interface FullProjectRepository {
    suspend fun sync()
    suspend fun getEmployee(employeeIds: List<String>): List<EmployeeResponse>
}