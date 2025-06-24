package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.kolesnik.potok.core.network.model.api.FlowStatus
import java.util.UUID

@Entity(
    tableName = "life_flows",
    foreignKeys = [ForeignKey(
        entity = LifeAreaEntity::class,
        parentColumns = ["id"],
        childColumns = ["areaId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LifeFlowEntity(
    @PrimaryKey val id: UUID,
    val areaId: UUID,
    val title: String,
    val style: String,
    val placement: Int?,
    val status: FlowStatus
)