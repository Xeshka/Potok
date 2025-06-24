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
        parentColumns = ["id"],
        childColumns = ["taskCardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChecklistTaskEntity(
    @PrimaryKey val id: UUID,
    val taskCardId: UUID,
    val title: String,
    val done: Boolean?,
    val placement: Int?,
    val deadline: OffsetDateTime?
)

@Entity(
    tableName = "checklist_task_responsibles",
    primaryKeys = ["checklistTaskId", "employeeId"]
)
data class ChecklistTaskResponsibleEntity(
    val checklistTaskId: UUID,
    val employeeId: String,
    val id: Long = 0
)