package ru.kolesnik.potok.core.model

data class Task(
    val id: TaskExternalId? = null,
    val title: String,
    val subtitle: String? = null,
    var mainOrder: Int? = null,
    val source: String? = null,
    val taskOwner: EmployeeId,
    val creationDate: IsuDate? = IsuDate.now(),
    val payload: TaskPayload?,
    val internalId: TaskInternalId? = null,
    val lifeAreaPlacement: Int? = null,
    val flowPlacement: Int? = null,
    val assignees: List<TaskAssignee>? = null,
    val commentCount: Int? = null,
    val attachmentCount: Int? = null,
    var checkList: List<ChecklistTask>? = null,
    var lifeAreaId: LifeAreaId? = null,
    var flowId: FlowId? = null
)


data class TaskPayload(
    var title: String? = null,
    val source: String? = null,
    var onMainPage: Boolean? = null,
    var deadline: IsuDate? = null,
    var lifeArea: String? = null,
    var lifeAreaId: LifeAreaId? = null,
    val subtitle: String? = null,
    var userEdit: Boolean? = null,
    var assignees: List<EmployeeId>? = null,
    var important: Boolean? = null,
    val messageId: String? = null,
    val fullMessage: String? = null,
    var description: String? = null,
    val priority: Int? = null,
    var userChangeAssignee: Boolean? = null,
    val organization: String? = null,
    val shortMessage: String? = null,
    var externalId: TaskExternalId? = null,
    val relatedAssignment: String? = null,
    var meanSource: String? = null,
    val id: String? = null,
)

data class TaskAssignee(
    val employeeId: EmployeeId,
    val complete: Boolean,
)