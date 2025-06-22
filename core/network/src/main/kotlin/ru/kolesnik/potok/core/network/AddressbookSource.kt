package ru.kolesnik.potok.core.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse


interface AddressbookSource {
    suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean,
    ): List<EmployeeResponse>
}

interface AuthApi {

    @GET("/api/service-addressbook/api/v1/addressbook/employee/search")
    suspend fun getEmployee(
        @Query("employeeNumber") employeeNumber: String,
        @Query("avatar") avatar: Boolean,
    ): List<EmployeeResponse>

    @GET("/")
    @Headers("Accept: text/html")
    suspend fun auth()
}
