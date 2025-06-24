package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

@Entity(
    tableName = "task_comments",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskCardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskCommentEntity(
    @PrimaryKey val id: UUID,
    val taskCardId: UUID,
    val parentCommentId: UUID?,
    val owner: String?,
    val text: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)