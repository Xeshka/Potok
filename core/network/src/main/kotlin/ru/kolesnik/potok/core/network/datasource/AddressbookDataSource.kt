package ru.kolesnik.potok.core.network.datasource

import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse

interface AddressbookDataSource {
    suspend fun getEmployee(
        employeeNumber: String,
        avatar: Boolean
    ): List<EmployeeResponse>
}