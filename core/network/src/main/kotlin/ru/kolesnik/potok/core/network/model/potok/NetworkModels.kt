package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.api.FlowStatus
import ru.kolesnik.potok.core.network.model.api.LifeAreaSharedInfo
import ru.kolesnik.potok.core.network.model.api.TaskPayload
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class NetworkLifeArea(
    val id: UUID,
    val title: String,
    val flows: List<NetworkLifeFlow>? = null,
    val style: String? = null,
    val tagsId: Long? = null,
    val placement: Int? = null,
    val isDefault: Boolean,
    val sharedInfo: LifeAreaSharedInfo? = null,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

@Serializable
data class NetworkLifeFlow(
    val id: UUID,
    val areaId: UUID,
    val title: String,
    val style: String,
    val placement: Int? = null,
    val status: FlowStatus? = null,
    val tasks: List<NetworkTask>? = null
)

@Serializable
data class NetworkTask(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val mainOrder: Int? = null,
    val source: String? = null,
    val taskOwner: String,
    val creationDate: OffsetDateTime,
    val payload: TaskPayload,
    val internalId: Long? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<NetworkTaskAssignee>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    val checkList: List<NetworkChecklistTask>? = null,
    val cardId: UUID
)

@Serializable
data class NetworkTaskAssignee(
    val employeeId: String,
    val complete: Boolean
)

@Serializable
data class NetworkChecklistTask(
    val id: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    val deadline: OffsetDateTime? = null
)

@Serializable
data class NetworkCreateTask(
    val lifeAreaId: UUID? = null,
    val flowId: UUID? = null,
    val payload: TaskPayload
)

@Serializable
data class PatchPayload(
    val title: String? = null,
    val source: String? = null,
    val deadline: OffsetDateTime? = null,
    val assignees: List<String>? = null,
    val important: Boolean? = null,
    val description: String? = null,
    val priority: Int? = null,
    val externalId: String? = null
)