package ru.kolesnik.potok.core.network.model.potok

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId

@Serializable
data class NetworkLifeAreaSharedInfo(
    val owner: EmployeeId,
    val readOnly: Boolean,
    val expiredDate: LocalDate? = null,
    val recipients: List<EmployeeId>,
)