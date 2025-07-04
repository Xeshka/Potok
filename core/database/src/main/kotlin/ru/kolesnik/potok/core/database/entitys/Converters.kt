package ru.kolesnik.potok.core.database.entitys

import android.util.Log
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.network.model.api.FlowStatus
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

class Converters {
    private val json = Json {
        serializersModule = customSerializersModule
        encodeDefaults = true    // Сериализует поля с дефолтными значениями
        ignoreUnknownKeys = true // Игнорирует лишние поля в JSON
        explicitNulls = false   // Не включает явные null-значения в JSON
        coerceInputValues = true // Автоматически преобразует некорректные значения
        isLenient = true        // Разрешает нестрогий JSON
    }

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

    @TypeConverter
    fun fromTaskPayload(value: TaskPayload): String {
        return try {
            json.encodeToString(value).also {
                if (it.isEmpty()) throw IllegalStateException("Serialization returned empty string")
            }
        } catch (e: Exception) {
            Log.e("Converters", "Failed to serialize TaskPayload: $value", e)
            throw IllegalArgumentException("Failed to serialize TaskPayload", e)
        }
    }

    @TypeConverter
    fun toTaskPayload(value: String): TaskPayload {
        return try {
            json.decodeFromString<TaskPayload>(value)
        } catch (e: Exception) {
            Log.e("Converters", "Failed to deserialize TaskPayload from: '$value'", e)
            throw IllegalArgumentException("Failed to deserialize TaskPayload", e)
        }
    }

    // FlowStatus enum
    @TypeConverter
    fun fromFlowStatus(value: FlowStatus?): String? = value?.name

    @TypeConverter
    fun toFlowStatus(value: String?): FlowStatus? = value?.let { 
        try {
            FlowStatus.valueOf(it)
        } catch (e: Exception) {
            FlowStatus.NEW
        }
    }

    // LifeAreaSharedInfo
    @TypeConverter
    fun fromLifeAreaSharedInfo(value: LifeAreaSharedInfo?): String? = value?.let { 
        try {
            json.encodeToString(it)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toLifeAreaSharedInfo(value: String?): LifeAreaSharedInfo? = value?.let { 
        try {
            json.decodeFromString<LifeAreaSharedInfo>(it)
        } catch (e: Exception) {
            null
        }
    }

    // List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? = value?.let { 
        try {
            json.encodeToString(it)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? = value?.let { 
        try {
            json.decodeFromString<List<String>>(it)
        } catch (e: Exception) {
            emptyList()
        }
    }
}