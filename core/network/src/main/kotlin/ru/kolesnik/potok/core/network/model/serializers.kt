package ru.kolesnik.potok.core.network.model

import dagger.Provides
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.NothingSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import javax.inject.Singleton

object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override val descriptor = PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        return OffsetDateTime.parse(decoder.decodeString(), formatter)
    }
}

object UuidSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        val string = decoder.decodeString()
        return try {
            UUID.fromString(string)
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Invalid UUID format: $string")
        }
    }
}


object UuidListSerializer : KSerializer<List<UUID>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUIDList", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: List<UUID>) {
        encoder.encodeString(value.joinToString(",") { it.toString() })
    }

    override fun deserialize(decoder: Decoder): List<UUID> {
        val input = decoder.decodeString()
        return if (input.isBlank()) emptyList()
        else input.split(",").map { UUID.fromString(it.trim()) }
    }
}


@OptIn(ExperimentalSerializationApi::class)
internal val customSerializersModule = SerializersModule {
    contextual(UUID::class, UuidSerializer)
    contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
    //contextual(
    //    kClass = List::class,
    //    provider = { typeArgs ->
    //        if (typeArgs.isNotEmpty() && typeArgs[0].descriptor == UUID::class) {
    //            UuidListSerializer
    //        } else {
    //            null
    //        }
    //    }
    //)
}
