package ru.kolesnik.potok.core.network.utils

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val serializersModule = SerializersModule {
    contextual(UUIDSerializer)
    contextual(OffsetDateTimeSerializer)
}