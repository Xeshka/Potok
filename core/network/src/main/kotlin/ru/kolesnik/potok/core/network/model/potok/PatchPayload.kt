package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId

@Serializable
data class PatchPayload(
    var title: String?,
    val source: String? = null,
    var deadline: String? = null,
    var assignees: List<EmployeeId>? = null,
    var important: Boolean? = false,
    var description: String? = null,
    val priority: Int? = null,
    var externalId: TaskExternalId? = null,
)