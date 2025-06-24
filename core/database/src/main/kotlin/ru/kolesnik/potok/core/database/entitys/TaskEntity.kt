package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    @PrimaryKey val id: UUID,
    val flowId: UUID?,
    val title: String,
    val subtitle: String?,
    val mainOrder: Int?,
    val source: String?,
    val taskOwner: String,
    val creationDate: OffsetDateTime?,
    val internalId: Long?,
    val lifeAreaPlacement: Int?,
    val flowPlacement: Int?,
    val commentCount: Int?,
    val attachmentCount: Int?,
    val lifeAreaId: UUID?
)

@Entity(
    tableName = "task_payloads",
    primaryKeys = ["taskId"],
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskPayloadEntity(
    val taskId: UUID,
    val title: String?,
    val source: String?,
    val onMainPage: Boolean?,
    val deadline: OffsetDateTime?,
    val lifeArea: String?,
    val lifeAreaId: UUID?,
    val subtitle: String?,
    val userEdit: Boolean?,
    val important: Boolean?,
    val messageId: String?,
    val fullMessage: String?,
    val description: String?,
    val priority: Int?,
    val userChangeAssignee: Boolean?,
    val organization: String?,
    val shortMessage: String?,
    val externalId: String?,
    val relatedAssignment: String?,
    val meanSource: String?,
    val id: String?
)

@Entity(
    tableName = "task_assignees",
    primaryKeys = ["taskId", "employeeId"]
)
data class TaskAssigneeEntity(
    val taskId: UUID,
    val employeeId: String,
    val complete: Boolean
)