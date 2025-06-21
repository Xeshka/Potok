package ru.kolesnik.potok.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithRelations(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val payload: TaskPayloadEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val assignees: List<TaskAssigneeEntity>
)