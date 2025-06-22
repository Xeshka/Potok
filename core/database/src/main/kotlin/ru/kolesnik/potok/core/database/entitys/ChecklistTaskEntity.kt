package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

@Entity(
    tableName = "checklist_tasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["cardId"],
        childColumns = ["taskCardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChecklistTaskEntity(
    @PrimaryKey val id: UUID,
    val taskCardId: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    val deadline: OffsetDateTime? = null
)