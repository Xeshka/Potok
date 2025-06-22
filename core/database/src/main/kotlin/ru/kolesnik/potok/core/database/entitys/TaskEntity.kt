package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.kolesnik.potok.core.model.TaskPayload
import java.time.OffsetDateTime
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = LifeAreaEntity::class,
            parentColumns = ["id"],
            childColumns = ["lifeAreaId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = LifeFlowEntity::class,
            parentColumns = ["id"],
            childColumns = ["flowId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TaskEntity(
    @PrimaryKey val cardId: UUID,
    val externalId: String?,
    val internalId: Long?,
    val title: String,
    val subtitle: String?,
    val mainOrder: Int?,
    val source: String?,
    val taskOwner: String,
    val creationDate: OffsetDateTime,
    val payload: TaskPayload,
    val lifeAreaId: UUID?,
    val flowId: UUID?,
    val lifeAreaPlacement: Int?,
    val flowPlacement: Int?,
    val commentCount: Int?,
    val attachmentCount: Int?,
    val deletedAt: OffsetDateTime? = null
)

@Entity(
    tableName = "task_assignees",
    primaryKeys = ["taskCardId", "employeeId"],
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["cardId"],
        childColumns = ["taskCardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskAssigneeEntity(
    val taskCardId: UUID,
    val employeeId: String,
    val complete: Boolean
)