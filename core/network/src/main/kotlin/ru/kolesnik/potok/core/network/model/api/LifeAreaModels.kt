package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.UuidSerializer
import java.util.UUID

// Request models
@Serializable
data class LifeAreaRq(
    val title: String,
    val style: String? = null,
    val tagsId: Long? = null,
    val placement: Int? = null,
    val isTheme: Boolean,
    val onlyPersonal: Boolean
)

@Serializable
data class LifeAreaMoveDTO(
    val lifeAreaId: UUID,
    val placement: Int? = null
)

@Serializable
data class LifeAreaPositionRq(
    val lifeAreaId: UUID,
    val position: Int
)

// Response models already exist in existing NetworkLifeArea