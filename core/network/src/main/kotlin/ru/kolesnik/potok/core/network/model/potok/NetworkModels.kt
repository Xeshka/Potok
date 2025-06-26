package ru.kolesnik.potok.core.network.model.potok

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Serializable
data class NetworkLifeArea(
    @Contextual val id: UUID,
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

@Serializable
data class NetworkLifeAreaSharedInfo(
    val owner: String,
    val readOnly: Boolean,
    @Contextual val expiredDate: OffsetDateTime? = null,
    val recipients: List<String>
)

@Serializable
data class NetworkLifeFlow(
    @Contextual val id: UUID,
    @Contextual val areaId: UUID,
    val title: String,
    val style: String,
    val placement: Int? = null,
    val status: String? = null,
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
    @Contextual val creationDate: OffsetDateTime,
    val payload: NetworkTaskPayload,
    val internalId: Long? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<NetworkTaskAssignee>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    val checkList: List<NetworkChecklistTask>? = null,
    @Contextual val cardId: UUID
)

@Serializable
data class NetworkTaskAssignee(
    val employeeId: String,
    val complete: Boolean
)

@Serializable
data class NetworkChecklistTask(
    @Contextual val id: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    @Contextual val deadline: OffsetDateTime? = null
)

@Serializable
data class NetworkTaskPayload(
    val title: String? = null,
    val source: String? = null,
    val onMainPage: Boolean? = null,
    @Contextual val deadline: OffsetDateTime? = null,
    val lifeArea: String? = null,
    @Contextual val lifeAreaId: UUID? = null,
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

@Serializable
data class NetworkOp(
    val insert: String? = null
)

@Serializable
data class NetworkCreateTask(
    @Contextual val lifeAreaId: UUID? = null,
    @Contextual val flowId: UUID? = null,
    val payload: NetworkTaskPayload
)

@Serializable
data class PatchPayload(
    val title: String? = null,
    val source: String? = null,
    @Contextual val deadline: OffsetDateTime? = null,
    val assignees: List<String>? = null,
    val important: Boolean? = null,
    val description: String? = null,
    val descriptionDelta: List<NetworkOp>? = null,
    val priority: Int? = null,
    val externalId: String? = null
)