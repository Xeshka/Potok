package ru.kolesnik.potok.core.model

data class ChecklistTask(
    val id: ChecklistTaskId,
    val title: String,
    val done: Boolean,
    val placement: Int,
    val responsibles: List<EmployeeId>? = emptyList(),
    val deadline: IsuDate? = null,
)