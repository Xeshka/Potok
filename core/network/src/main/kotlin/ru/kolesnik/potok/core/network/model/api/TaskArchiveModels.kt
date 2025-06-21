package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.OffsetDateTimeSerializer
import ru.kolesnik.potok.core.network.model.UuidSerializer
import java.time.OffsetDateTime
import java.util.UUID

// Request models
@Serializable
data class TaskExternalIds(
    val taskIds: List<String>
)

// Response models
@Serializable
data class TaskArchiveEntryDTO(
    val taskExternalId: String,
    val title: String,
    val subtitle: String? = null,
    val source: String? = null,
    val description: String? = null,
    val payload: TaskPayload,
    val assignees: List<TaskAssigneeRs>,
    val status: String,
    val deletedAt: OffsetDateTime? = null,
    val taskOwner: String,
    val lifeAreaId: UUID,
    val flowId: UUID,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null
)

@Serializable
data class TaskArchivePageDTO(
    val limit: Int? = null,
    val offset: Int? = null,
    val total: Int? = null,
    val items: List<TaskArchiveEntryDTO>
)

@Serializable
data class TaskAssigneeRs(
    val employeeId: String,
    val complete: Boolean
)

@Serializable
data class TaskPayload(
var title: String? = null,
val source: String? = null,
var onMainPage: Boolean? = null,
var deadline: OffsetDateTime? = null,
var lifeArea: String? = null,
var lifeAreaId: UUID? = null,
val subtitle: String? = null,
var userEdit: Boolean? = null,
var assignees: List<String>? = null,
var important: Boolean? = null,
val messageId: String? = null,
val fullMessage: String? = null,
var description: String? = null,
val priority: Int? = null,
var userChangeAssignee: Boolean? = null,
val organization: String? = null,
val shortMessage: String? = null,
var externalId: String? = null,
val relatedAssignment: String? = null,
var meanSource: String? = null,
val id: String? = null,
)