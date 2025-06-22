package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.OffsetDateTime
import kotlinx.serialization.Contextual

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
    @Contextual val deletedAt: OffsetDateTime? = null,
    val taskOwner: String,
    @Contextual val lifeAreaId: UUID,
    @Contextual val flowId: UUID,
    val commentCount: Int,
    val attachmentCount: Int
)

@Serializable
data class TaskArchivePageDTO(
    val limit: Int,
    val offset: Int,
    val total: Int,
    val items: List<TaskArchiveEntryDTO>
)

@Serializable
data class TaskExternalIds(
    val taskIds: List<String>
)