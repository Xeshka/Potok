package ru.kolesnik.potok.core.model

data class Employee(
    val employeeNumber: String,
    val timezone: String,
    val terBank: String,
    val employeeId: String,
    val lastName: String,
    val firstName: String,
    val middleName: String? = null,
    val position: String,
    val mainEmail: String,
    val avatar: String? = null,
)