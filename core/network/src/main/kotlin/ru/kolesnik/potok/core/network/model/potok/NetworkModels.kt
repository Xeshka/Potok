package ru.kolesnik.potok.core.network.model.potok

import ru.kolesnik.potok.core.network.model.api.FlowStatus
import java.time.OffsetDateTime
import java.util.UUID

data class NetworkLifeArea(
    val id: UUID,
    val title: String,
    val flows: List<NetworkLifeFlow>? = null,
    val style: String? = null,
    val tagsId: Long? = null,
    val placement: Int? = null,
    val isDefault: Boolean,
    val sharedInfo: NetworkLifeAreaSharedInfo? = null,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

data class NetworkLifeAreaSharedInfo(
    val owner: String,
    val readOnly: Boolean,
    val expiredDate: String? = null,
    val recipients: List<String>
)

data class NetworkLifeFlow(
    val id: UUID,
    val areaId: UUID,
    val title: String,
    val style: String,
    val placement: Int? = null,
    val status: FlowStatus? = null,
    val tasks: List<NetworkTask>? = null
)

data class NetworkTask(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val mainOrder: Int? = null,
    val source: String? = null,
    val taskOwner: String,
    val creationDate: OffsetDateTime,
    val payload: NetworkTaskPayload,
    val internalId: Long? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<NetworkTaskAssignee>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    val checkList: List<NetworkChecklistTask>? = null,
    val cardId: UUID
)

data class NetworkTaskAssignee(
    val employeeId: String,
    val complete: Boolean
)

data class NetworkTaskPayload(
    val title: String? = null,
    val source: String? = null,
    val onMainPage: Boolean? = null,
    val deadline: OffsetDateTime? = null,
    val lifeArea: String? = null,
    val lifeAreaId: UUID? = null,
    val subtitle: String? = null,
    val userEdit: Boolean? = null,
    val assignees: List<String>? = null,
    val important: Boolean? = null,
    val messageId: String? = null,
    val fullMessage: String? = null,
    val description: String? = null,
    val descriptionDelta: List<NetworkOp>? = null,
    val priority: Int? = null,
    val userChangeAssignee: Boolean? = null,
    val organization: String? = null,
    val shortMessage: String? = null,
    val externalId: String? = null,
    val relatedAssignment: String? = null,
    val meanSource: String? = null,
    val id: String? = null
)

data class NetworkOp(
    val insert: String? = null,
    val attributes: Map<String, Any>? = null
)

data class NetworkChecklistTask(
    val id: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    val deadline: OffsetDateTime? = null
)

data class NetworkCreateTask(
    val lifeAreaId: UUID? = null,
    val flowId: UUID? = null,
    val payload: NetworkTaskPayload
)

data class PatchPayload(
    val title: String? = null,
    val source: String? = null,
    val deadline: OffsetDateTime? = null,
    val assignees: List<String>? = null,
    val important: Boolean? = null,
    val description: String? = null,
    val descriptionDelta: List<NetworkOp>? = null,
    val priority: Int? = null,
    val externalId: String? = null
)