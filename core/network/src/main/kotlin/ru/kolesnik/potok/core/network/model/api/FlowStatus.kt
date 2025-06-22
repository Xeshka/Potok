package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlinx.serialization.Contextual

@Serializable
enum class FlowStatus {
    NEW,
    IN_PROGRESS,
    COMPLETED,
    CUSTOM
}

@Serializable
data class LifeFlowDTO(
    @Contextual val id: UUID,
    @Contextual val areaId: UUID,
    val title: String,
    val style: String,
    val placement: Int? = null,
    val status: FlowStatus? = null,
    val tasks: List<TaskRs>? = null
)

@Serializable
data class LifeFlowRq(
    val title: String,
    val style: String? = null,
    val placement: Int? = null
)

@Serializable
data class LifeFlowMoveDTO(
    @Contextual val flowId: UUID,
    val placement: Int
)

@Serializable
data class LifeFlowPosition(
    @Contextual val flowId: UUID,
    val position: Int,
    val status: FlowStatus
)

@Serializable
data class FlowPositionRq(
    @Contextual val flowId: UUID,
    val position: Int
)