package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID

// Request models
@Serializable
data class ChecklistRq(
    val checklist: List<ChecklistTaskRq>
)

@Serializable
data class ChecklistTaskRq(
    val title: String,
    val done: Boolean? = null,
    val responsibles: List<String>? = null,
    val deadline: OffsetDateTime? = null
)

@Serializable
data class ChecklistTaskTitleRq(
    val title: String
)

@Serializable
data class ChecklistTaskDeadlineRq(
    val deadline: OffsetDateTime? = null
)

@Serializable
data class ChecklistTaskResponsiblesRq(
    val responsibles: List<String>? = null
)

@Serializable
data class ChecklistTaskMoveRq(
    val checklistTaskId: UUID,
    val placement: Int? = null
)

// Response models
@Serializable
data class ChecklistDTO(
    val checklist: List<ChecklistTaskDTO>
)

@Serializable
data class ChecklistTaskDTO(
    val id: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    val deadline: OffsetDateTime? = null
)