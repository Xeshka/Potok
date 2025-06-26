package ru.kolesnik.potok.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse

interface AddressbookApi {
    @GET("/api/service-addressbook/api/v1/addressbook/employee/search")
    suspend fun getEmployee(
        @Query("employeeNumber") employeeNumber: String,
        @Query("avatar") avatar: Boolean,
    ): List<EmployeeResponse>
}

