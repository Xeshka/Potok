package ru.kolesnik.potok.core.model

data class TaskMain(
    val id: TaskExternalId,
    val title: String,
    val source: String? = null,
    val taskOwner: EmployeeId,
    val creationDate: IsuDate?,
    val deadline: IsuDate?,
    val internalId: TaskInternalId? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<TaskAssignee>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    var lifeAreaId: LifeAreaId? = null,
    var flowId: FlowId? = null
)