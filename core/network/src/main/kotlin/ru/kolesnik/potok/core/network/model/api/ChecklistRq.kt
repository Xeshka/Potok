package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.OffsetDateTime
import kotlinx.serialization.Contextual

@Serializable
data class ChecklistRq(
    val checklist: List<ChecklistTaskRq>
)

@Serializable
data class ChecklistDTO(
    val checklist: List<ChecklistTaskDTO>
)

@Serializable
data class ChecklistTaskRq(
    val title: String,
    val done: Boolean? = null,
    val responsibles: List<String>? = null,
    @Contextual val deadline: OffsetDateTime? = null
)

@Serializable
data class ChecklistTaskDTO(
    @Contextual val id: UUID,
    val title: String,
    val done: Boolean? = null,
    val placement: Int? = null,
    val responsibles: List<String>? = null,
    @Contextual val deadline: OffsetDateTime? = null
)

@Serializable
data class ChecklistTaskTitleRq(
    val title: String
)

@Serializable
data class ChecklistTaskDeadlineRq(
    @Contextual val deadline: OffsetDateTime? = null
)

@Serializable
data class ChecklistTaskResponsiblesRq(
    val responsibles: List<String>? = null
)

@Serializable
data class ChecklistTaskMoveRq(
    @Contextual val checklistTaskId: UUID,
    val placement: Int
)