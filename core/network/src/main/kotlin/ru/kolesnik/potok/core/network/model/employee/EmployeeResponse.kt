package ru.kolesnik.potok.core.network.model.employee

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeResponse(
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
    val hrInfo: HrInfo? = null,
    val workSchedule: WorkSchedule? = null,
    val emails: List<Email> = emptyList()
) {
    @Serializable
    data class HrInfo(
        val position: String? = null,
        val department: String? = null,
        val departmentId: String? = null,
        val departmentTree: String? = null,
        val departmentIdTree: String? = null
    )

    @Serializable
    data class WorkSchedule(
        val typeId: String? = null,
        val startDate: Long? = null,
        val endDate: Long? = null
    )

    @Serializable
    data class Email(
        val type: String,
        val address: String,
        val isMain: Boolean
    )
}