package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.UuidSerializer
import java.util.UUID

// Request models
@Serializable
data class LifeFlowRq(
    val title: String,
    val style: String? = null,
    val placement: Int? = null
)

@Serializable
data class LifeFlowMoveDTO(
    val flowId: UUID,
    val placement: Int? = null
)

@Serializable
data class FlowPositionRq(
    val flowId: UUID,
    val position: Int? = null
)

// Response models already exist in existing NetworkLifeFlow