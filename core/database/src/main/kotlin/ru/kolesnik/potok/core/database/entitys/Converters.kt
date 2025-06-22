package ru.kolesnik.potok.core.database.entitys

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.model.TaskPayload

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    // UUID
    @TypeConverter
    fun fromUUID(value: UUID?) = value?.toString()

    @TypeConverter
    fun toUUID(value: String?) = value?.let { UUID.fromString(it) }

    // OffsetDateTime
    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?) = value?.toString()

    @TypeConverter
    fun toOffsetDateTime(value: String?) = value?.let { OffsetDateTime.parse(it) }

    // LocalDate
    @TypeConverter
    fun fromLocalDate(value: LocalDate?) = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?) = value?.let { LocalDate.parse(it) }

    // TaskPayload
    @TypeConverter
    fun fromTaskPayload(value: TaskPayload?) = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toTaskPayload(value: String?) = value?.let { json.decodeFromString<TaskPayload>(it) }

    // FlowStatus enum
    @TypeConverter
    fun fromFlowStatus(value: FlowStatus?) = value?.name

    @TypeConverter
    fun toFlowStatus(value: String?) = value?.let { FlowStatus.valueOf(it) }

    // LifeAreaSharedInfo
    @TypeConverter
    fun fromLifeAreaSharedInfo(value: LifeAreaSharedInfo?) = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toLifeAreaSharedInfo(value: String?) = value?.let { json.decodeFromString<LifeAreaSharedInfo>(it) }

    // List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?) = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStringList(value: String?) = value?.let { json.decodeFromString<List<String>>(it) }

}