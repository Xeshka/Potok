package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.AddressbookApi
import ru.kolesnik.potok.core.network.datasource.AddressbookDataSource
import javax.inject.Inject

class RetrofitAddressbookDataSource @Inject constructor(
    private val api: AddressbookApi
) : AddressbookDataSource {
    override suspend fun getEmployee(
        employeeNumber: String,
        avatar: Boolean
    ) = api.getEmployee(employeeNumber, avatar)
}