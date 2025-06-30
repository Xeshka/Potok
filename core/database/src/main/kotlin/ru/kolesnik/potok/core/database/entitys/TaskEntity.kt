package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.model.EmployeeId
import ru.kolesnik.potok.core.model.IsuDate
import ru.kolesnik.potok.core.model.LifeAreaId
import ru.kolesnik.potok.core.model.TaskExternalId
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
@TypeConverters(Converters::class)
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
    val payload: TaskPayload?,
    val lifeAreaId: UUID?,
    var flowId: UUID?,
    val lifeAreaPlacement: Int?,
    val flowPlacement: Int?,
    val commentCount: Int?,
    val attachmentCount: Int?,
    val deletedAt: OffsetDateTime? = null
)

@Entity(
    tableName = "task_assignees",
    primaryKeys = ["taskCardId", "employeeId"]
)
@TypeConverters(Converters::class)
data class TaskAssigneeEntity(
    val taskCardId: UUID,
    val employeeId: String,
    val complete: Boolean
)

@Serializable
@TypeConverters(Converters::class)
data class TaskPayload(
    var title: String,
    val source: String? = null,
    var onMainPage: Boolean?,
    @Contextual var deadline: IsuDate? = null,
    var lifeArea: String? = null,
    @Contextual var lifeAreaId: LifeAreaId? = null,
    val subtitle: String? = null,
    var userEdit: Boolean? = null,
    var assignees: List<EmployeeId>? = null,
    var important: Boolean? = null,
    val messageId: String? = null,
    val fullMessage: String? = null,
    var description: String? = null,
    val priority: Int? = null,
    var userChangeAssignee: Boolean? = null,
    val organization: String? = null,
    val shortMessage: String? = null,
    var externalId: TaskExternalId? = null,
    val relatedAssignment: String? = null,
    var meanSource: String? = null,
    val id: String? = null,
)