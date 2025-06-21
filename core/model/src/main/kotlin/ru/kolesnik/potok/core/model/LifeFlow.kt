package ru.kolesnik.potok.core.model

data class LifeFlow(
    val id: String,
    val areaId: String,
    val title: String,
    val style: String,
    val placement: Int?,
    val status: FlowStatus
)