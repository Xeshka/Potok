package ru.kolesnik.potok.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "life_areas")
data class LifeAreaEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    val title: String,
    val style: String?,
    val tagsId: Int?,
    val placement: Int?,
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean,
    @ColumnInfo(name = "is_theme")
    val isTheme: Boolean,
    @ColumnInfo(name = "only_personal")
    val onlyPersonal: Boolean
)

@Entity(
    tableName = "life_area_shared_info",
    foreignKeys = [ForeignKey(
        entity = LifeAreaEntity::class,
        parentColumns = ["id"],
        childColumns = ["life_area_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LifeAreaSharedInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "life_area_id")
    val lifeAreaId: String,
    val owner: String,
    @ColumnInfo(name = "read_only")
    val readOnly: Boolean,
    @ColumnInfo(name = "expired_date")
    val expiredDate: String?
)

@Entity(tableName = "life_area_shared_info_recipients")
data class LifeAreaSharedInfoRecipientEntity(
    @ColumnInfo(name = "shared_info_id")
    val sharedInfoId: String,
    @ColumnInfo(name = "employee_id")
    val employeeId: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}