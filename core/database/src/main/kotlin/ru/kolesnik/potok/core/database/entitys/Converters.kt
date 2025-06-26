package ru.kolesnik.potok.core.database.entitys

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.model.TaskPayload
import ru.kolesnik.potok.core.network.model.api.FlowStatus

import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class Converters {
    private val json = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
        coerceInputValues = true
    }

    // UUID
    @TypeConverter
    fun fromUUID(value: UUID?): String? = value?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = try {
        value?.let { UUID.fromString(it) }
    } catch (e: Exception) {
        null
    }

    // OffsetDateTime
    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): String? = value?.toString()

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? = try {
        value?.let { OffsetDateTime.parse(it) }
    } catch (e: Exception) {
        null
    }

    // LocalDate
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = try {
        value?.let { LocalDate.parse(it) }
    } catch (e: Exception) {
        null
    }

    // TaskPayload
    @TypeConverter
    fun fromTaskPayload(value: TaskPayload?): String? = try {
        value?.let { json.encodeToString(TaskPayload.serializer(), it) }
    } catch (e: Exception) {
        null
    }

    @TypeConverter
    fun toTaskPayload(value: String?): TaskPayload? = try {
        value?.let { json.decodeFromString(TaskPayload.serializer(), it) }
    } catch (e: Exception) {
        null
    }

    // FlowStatus enum
    @TypeConverter
    fun fromFlowStatus(value: FlowStatus?): String? = value?.name

    @TypeConverter
    fun toFlowStatus(value: String?): FlowStatus? = try {
        value?.let { FlowStatus.valueOf(it) }
    } catch (e: Exception) {
        null
    }

    // LifeAreaSharedInfo
    @TypeConverter
    fun fromLifeAreaSharedInfo(value: LifeAreaSharedInfo?): String? = try {
        value?.let { json.encodeToString(LifeAreaSharedInfo.serializer(), it) }
    } catch (e: Exception) {
        null
    }

    @TypeConverter
    fun toLifeAreaSharedInfo(value: String?): LifeAreaSharedInfo? = try {
        value?.let { json.decodeFromString(LifeAreaSharedInfo.serializer(), it) }
    } catch (e: Exception) {
        null
    }

    // List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? = try {
        value?.let { json.encodeToString(List.serializer(String.serializer()), it) }
    } catch (e: Exception) {
        null
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? = try {
        value?.let { json.decodeFromString(List.serializer(String.serializer()), it) }
    } catch (e: Exception) {
        null
    }
}