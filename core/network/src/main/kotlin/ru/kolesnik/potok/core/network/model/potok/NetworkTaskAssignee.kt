package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId

@Serializable
data class NetworkTaskAssignee(
    val employeeId: EmployeeId,
    val complete: Boolean,
)