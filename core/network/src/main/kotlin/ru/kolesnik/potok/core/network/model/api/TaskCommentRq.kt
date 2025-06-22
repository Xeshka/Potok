package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.OffsetDateTime
import kotlinx.serialization.Contextual

@Serializable
data class TaskCommentRq(
    @Contextual val parentCommentId: UUID? = null,
    val text: String
)

@Serializable
data class TaskCommentDTO(
    @Contextual val id: UUID,
    @Contextual val parentCommentId: UUID? = null,
    val owner: String? = null,
    val text: String,
    @Contextual val createdAt: OffsetDateTime,
    @Contextual val updatedAt: OffsetDateTime
)

@Serializable
data class TaskCommentPageDTO(
    val limit: Int? = null,
    val offset: Int? = null,
    val total: Int? = null,
    val items: List<TaskCommentDTO>
)