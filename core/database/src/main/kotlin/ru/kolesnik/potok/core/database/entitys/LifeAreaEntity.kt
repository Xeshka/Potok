package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "life_areas")
data class LifeAreaEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val style: String?,
    val tagsId: Long?,
    val placement: Int?,
    val isDefault: Boolean,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

@Entity(
    tableName = "life_area_shared_info",
    primaryKeys = ["lifeAreaId"],
    foreignKeys = [
        ForeignKey(
            entity = LifeAreaEntity::class,
            parentColumns = ["id"],
            childColumns = ["lifeAreaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LifeAreaSharedInfoEntity(
    val lifeAreaId: UUID,
    val owner: String,
    val readOnly: Boolean,
    val expiredDate: LocalDate?
)

@Entity(
    tableName = "life_area_shared_info_recipients",
    primaryKeys = ["sharedInfoId", "employeeId"]
)
data class LifeAreaSharedInfoRecipientEntity(
    val sharedInfoId: UUID,
    val employeeId: String,
    val id: Long = 0
)