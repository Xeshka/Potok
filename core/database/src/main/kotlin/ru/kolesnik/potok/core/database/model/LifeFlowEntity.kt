package ru.kolesnik.potok.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "life_flows",
    foreignKeys = [ForeignKey(
        entity = LifeAreaEntity::class,
        parentColumns = ["id"],
        childColumns = ["area_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("area_id")]
)
data class LifeFlowEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "area_id")
    val areaId: String,
    val title: String,
    val style: String,
    val placement: Int?,
    val status: String
)