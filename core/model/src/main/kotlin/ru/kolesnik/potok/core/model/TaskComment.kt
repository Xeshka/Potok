package ru.kolesnik.potok.core.model

import java.time.OffsetDateTime
import java.util.UUID

data class TaskComment(
    val id: UUID,
    val parentCommentId: UUID? = null,
    val owner: String? = null,
    val text: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)