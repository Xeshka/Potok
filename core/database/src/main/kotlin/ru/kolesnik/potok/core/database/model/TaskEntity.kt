package ru.kolesnik.potok.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "flow_id")
    val flowId: String?,
    val title: String,
    val subtitle: String?,
    @ColumnInfo(name = "main_order")
    val mainOrder: Int?,
    val source: String?,
    @ColumnInfo(name = "task_owner")
    val taskOwner: String,
    @ColumnInfo(name = "creation_date")
    val creationDate: String?,
    @ColumnInfo(name = "internal_id")
    val internalId: Long?,
    @ColumnInfo(name = "life_area_placement")
    val lifeAreaPlacement: Int?,
    @ColumnInfo(name = "flow_placement")
    val flowPlacement: Int?,
    @ColumnInfo(name = "comment_count")
    val commentCount: Int?,
    @ColumnInfo(name = "attachment_count")
    val attachmentCount: Int?,
    @ColumnInfo(name = "life_area_id")
    val lifeAreaId: LifeAreaId?,
)

@Entity(
    tableName = "task_payloads",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["task_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskPayloadEntity(
    @PrimaryKey
    @ColumnInfo(name = "task_id")
    val taskId: String,
    val title: String?,
    val source: String?,
    @ColumnInfo(name = "on_main_page")
    val onMainPage: Boolean?,
    val deadline: String?,
    @ColumnInfo(name = "life_area")
    val lifeArea: String?,
    @ColumnInfo(name = "life_area_id")
    val lifeAreaId: String?,
    val subtitle: String?,
    @ColumnInfo(name = "user_edit")
    val userEdit: Boolean?,
    val important: Boolean?,
    @ColumnInfo(name = "message_id")
    val messageId: String?,
    @ColumnInfo(name = "full_message")
    val fullMessage: String?,
    val description: String?,
    val priority: Int?,
    @ColumnInfo(name = "user_change_assignee")
    val userChangeAssignee: Boolean?,
    val organization: String?,
    @ColumnInfo(name = "short_message")
    val shortMessage: String?,
    @ColumnInfo(name = "external_id")
    val externalId: String?,
    @ColumnInfo(name = "related_assignment")
    val relatedAssignment: String?,
    @ColumnInfo(name = "mean_source")
    val meanSource: String?,
    val id: String?
)

@Entity(
    tableName = "task_assignees",
    primaryKeys = ["task_id", "employee_id"]
)
data class TaskAssigneeEntity(
    @ColumnInfo(name = "task_id")
    val taskId: String,

    @ColumnInfo(name = "employee_id")
    val employeeId: String,

    val complete: Boolean
)