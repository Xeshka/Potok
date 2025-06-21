package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.ChecklistTaskId
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.IsuDate

@Serializable
data class NetworkChecklistTask(
    val id: ChecklistTaskId,
    val title: String,
    val done: Boolean,
    val placement: Int,
    val responsibles: List<EmployeeId>? = emptyList(),
    val deadline: IsuDate? = null,
)