package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse

interface SyncFullDataSource {
    suspend fun gtFullNew(): List<LifeAreaDTO>
    suspend fun getEmployee(employeeNumbers: List<EmployeeId>, avatar: Boolean): List<EmployeeResponse>
}