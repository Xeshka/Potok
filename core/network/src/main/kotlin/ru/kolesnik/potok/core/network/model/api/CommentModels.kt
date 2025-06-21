package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

// Request models
@Serializable
data class TaskCommentRq(
    val parentCommentId: UUID? = null,
    val text: String
)

@Serializable
data class SearchQuery(
    val query: String
)

// Response models
@Serializable
data class TaskCommentDTO(
    val id: UUID,
    val parentCommentId: UUID? = null,
    val owner: String? = null,
    val text: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

@Serializable
data class TaskCommentPageDTO(
    val limit: Int? = null,
    val offset: Int? = null,
    val total: Int? = null,
    val items: List<TaskCommentDTO>
)

@Serializable
data class SearchRs(
    val commentIds: List<UUID>
)