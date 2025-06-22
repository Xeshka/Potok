package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.OffsetDateTime
import kotlinx.serialization.Contextual

@Serializable
data class TaskId(
    val id: Long? = null,
    val externalId: String
)

@Serializable
data class TaskAssigneeRs(
    val employeeId: String,
    val complete: Boolean
)

@Serializable
data class TaskPayload(
    val title: String? = null,
    val source: String? = null,
    val onMainPage: Boolean? = null,
    @Contextual val deadline: OffsetDateTime? = null,
    val lifeArea: String? = null,
    @Contextual val lifeAreaId: UUID? = null,
    val reminder: TaskPayloadReminder? = null,
    val subtitle: String? = null,
    val userEdit: Boolean? = null,
    val assignees: List<String>? = null,
    val important: Boolean? = null,
    val messageId: String? = null,
    val fullMessage: String? = null,
    val description: String? = null,
    val descriptionDelta: List<Op>? = null,
    val priority: Int? = null,
    val taskObjects: TaskPayloadTaskObjects? = null,
    val userChangeAssignee: Boolean? = null,
    val organization: String? = null,
    val shortMessage: String? = null,
    val externalId: String? = null,
    val relatedAssignment: String? = null,
    val _source: UploadTaskData? = null,
    val meanSource: String? = null,
    val id: String? = null
)

@Serializable
data class TaskPayloadReminder(
    val text: String? = null,
    val alertBefore: Int? = null,
    @Contextual val reminderDate: OffsetDateTime? = null
)

@Serializable
data class TaskPayloadTaskObjects(
    val video: PayloadTaskObjectItem? = null,
    val person: PayloadTaskObjectItem? = null,
    val location: PayloadTaskObjectItem? = null,
    val relative: PayloadTaskObjectItem? = null,
    val organization: PayloadTaskObjectItem? = null
)

@Serializable
data class PayloadTaskObjectItem(
    val title: String? = null
)

@Serializable
data class UploadTaskData(
    val category: String? = null,
    val duration: Int? = null,
    val priority: Int? = null,
    val strategySection: String? = null,
    val meetingDuration: Int? = null
)

@Serializable
data class TaskRq(
    @Contextual val lifeAreaId: UUID? = null,
    @Contextual val flowId: UUID? = null,
    val payload: TaskPayload
)

@Serializable
data class TaskRs(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val mainOrder: Int? = null,
    val source: String? = null,
    val taskOwner: String,
    @Contextual val creationDate: OffsetDateTime,
    val payload: TaskPayload,
    val internalId: Long? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<TaskAssigneeRs>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    val checkList: List<ChecklistTaskDTO>? = null,
    @Contextual val cardId: UUID
)