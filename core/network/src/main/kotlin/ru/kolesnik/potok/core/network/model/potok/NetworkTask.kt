package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.IsuDate
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.TaskInternalId

@Serializable
data class NetworkTask(
    val id: TaskExternalId,
    val title: String,
    val subtitle: String? = null,
    var mainOrder: Int? = null,
    val source: String? = null,
    val taskOwner: EmployeeId,
    val creationDate: IsuDate,
    val payload: NetworkTaskPayload?,
    val internalId: TaskInternalId? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<NetworkTaskAssignee>? = null,
    val metrics: List<NetworkMetric>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    var checkList: List<NetworkChecklistTask>? = null
)