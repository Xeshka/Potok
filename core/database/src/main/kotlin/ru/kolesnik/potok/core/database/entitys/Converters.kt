package ru.kolesnik.potok.core.database.entitys

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.model.TaskPayload
import ru.kolesnik.potok.core.network.model.api.FlowStatus

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    // UUID
    @TypeConverter
    fun fromUUID(value: UUID?): String? = value?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }

    // OffsetDateTime
    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): String? = value?.toString()

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? = value?.let { OffsetDateTime.parse(it) }

    // LocalDate
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // TaskPayload
    @TypeConverter
    fun fromTaskPayload(value: TaskPayload?): String? = value?.let { json.encodeToString(value) }

    @TypeConverter
    fun toTaskPayload(value: String?): TaskPayload? = value?.let { json.decodeFromString<TaskPayload>(it) }

    // FlowStatus enum
    @TypeConverter
    fun fromFlowStatus(value: FlowStatus?): String? = value?.name

    @TypeConverter
    fun toFlowStatus(value: String?): FlowStatus? = value?.let { FlowStatus.valueOf(it) }

    // LifeAreaSharedInfo
    @TypeConverter
    fun fromLifeAreaSharedInfo(value: LifeAreaSharedInfo?): String? = value?.let { json.encodeToString(value) }

    @TypeConverter
    fun toLifeAreaSharedInfo(value: String?): LifeAreaSharedInfo? = value?.let { json.decodeFromString<LifeAreaSharedInfo>(it) }

    // List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.let { json.encodeToString(it) }

    @TypeConverter
    fun toStringList(value: String?): List<String>? = value?.let { json.decodeFromString<List<String>>(it) }
}