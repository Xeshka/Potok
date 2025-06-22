package ru.kolesnik.potok.core.database.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kolesnik.potok.core.network.model.api.LifeAreaSharedInfo


import java.util.UUID

@Entity(tableName = "life_areas")
@TypeConverters(Converters::class)
data class LifeAreaEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val style: String?,
    val tagsId: Long?,
    val placement: Int?,
    val isDefault: Boolean,
    val sharedInfo: LifeAreaSharedInfo?,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)