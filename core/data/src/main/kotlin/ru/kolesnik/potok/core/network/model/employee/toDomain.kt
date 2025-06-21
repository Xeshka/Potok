package ru.kolesnik.potok.core.network.model.employee

import ru.kolesnik.potok.core.model.Employee

fun EmployeeResponse.toDomain() = Employee(
    employeeNumber = employeeNumber,
    timezone = timezone,
    terBank = terBank,
    employeeId = employeeId,
    lastName = lastName,
    firstName = firstName,
    middleName = middleName,
    position = position,
    mainEmail = mainEmail,
    avatar = avatar
)