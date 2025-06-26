package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.datasource.AddressbookDataSource
import ru.kolesnik.potok.core.network.datasource.LifeAreaDataSource
import ru.kolesnik.potok.core.network.datasource.TaskDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import javax.inject.Inject

class RetrofitSyncFullDataSource @Inject constructor(
    private val lifeAreaDataSource: LifeAreaDataSource,
    private val employeeDataSource: AddressbookDataSource,
    private val taskDataSource: TaskDataSource
) : SyncFullDataSource {

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return lifeAreaDataSource.getFullLifeAreas()
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return employeeNumbers.flatMap { employeeNumber ->
            employeeDataSource.getEmployee(employeeNumber, avatar)
        }
    }

}