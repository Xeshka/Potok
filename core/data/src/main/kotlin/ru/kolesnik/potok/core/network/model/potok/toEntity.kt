package ru.kolesnik.potok.core.network.model.potok

import ru.kolesnik.potok.core.database.model.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.model.ChecklistTaskResponsibleEntity
import ru.kolesnik.potok.core.database.model.LifeAreaEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoRecipientEntity
import ru.kolesnik.potok.core.database.model.LifeFlowEntity
import ru.kolesnik.potok.core.database.model.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.model.TaskEntity
import ru.kolesnik.potok.core.database.model.TaskPayloadEntity
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.network.model.FlowStatus

fun NetworkLifeArea.toEntities(): LifeAreaEntities {
    val lifeAreaEntity = this.toLifeAreaEntity()
    val (sharedInfoEntity, sharedRecipients) = this.sharedInfo.toSharedInfoEntities(lifeAreaEntity.id)
    val flowEntities = this.flows?.map { it.toFlowEntities(lifeAreaEntity.id) } ?: emptyList()

    return LifeAreaEntities(
        lifeArea = lifeAreaEntity,
        sharedInfo = sharedInfoEntity,
        sharedRecipients = sharedRecipients,
        flows = flowEntities
    )
}

private fun NetworkLifeArea.toLifeAreaEntity() = LifeAreaEntity(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

private fun NetworkLifeAreaSharedInfo?.toSharedInfoEntities(lifeAreaId: String): Pair<LifeAreaSharedInfoEntity?, List<LifeAreaSharedInfoRecipientEntity>> {
    if (this == null) return Pair(null, emptyList())

    return Pair(
        LifeAreaSharedInfoEntity(
            lifeAreaId = lifeAreaId,
            owner = owner,
            readOnly = readOnly,
            expiredDate = expiredDate.toString()
        ),
        recipients.map {
            LifeAreaSharedInfoRecipientEntity(
                sharedInfoId = lifeAreaId,
                employeeId = it
            )
        }
    )
}

fun NetworkLifeFlow.toFlowEntities(areaId: String): FlowEntities {
    val flowEntity = this.toLifeFlowEntity(areaId)
    val taskEntities = this.tasks?.map {
        it.toTaskEntities(flowEntity.id, areaId)
    } ?: emptyList()

    return FlowEntities(
        flow = flowEntity,
        tasks = taskEntities
    )
}

private fun NetworkLifeFlow.toLifeFlowEntity(areaId: String) = LifeFlowEntity(
    id = id,
    areaId = areaId,
    title = title,
    style = style,
    placement = placement,
    status = (status ?: FlowStatus.NEW).name
)

fun NetworkTask.toTaskEntities(
    flowId: String?,
    lifeAreaId: String?
): TaskEntities {
    val taskEntity = this.toTaskEntity(flowId, lifeAreaId)
    return TaskEntities(
        task = taskEntity,
        payload = payload.toTaskPayloadEntity(taskEntity.id),
        assignees = assignees.toTaskAssigneeEntities(taskEntity.id),
        checklists = checkList.toChecklistEntities(taskEntity.id)
    )
}

private fun NetworkTask.toTaskEntity(
    flowId: String?,
    lifeAreaId: String?
) = TaskEntity(
    id = id,
    flowId = flowId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = lifeAreaId
)

private fun NetworkTaskPayload?.toTaskPayloadEntity(taskId: String) = this?.let {
    TaskPayloadEntity(
        taskId = taskId,
        title = title,
        source = source,
        onMainPage = onMainPage,
        deadline = deadline,
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId,
        subtitle = subtitle,
        userEdit = userEdit,
        important = important,
        messageId = messageId,
        fullMessage = fullMessage,
        description = description,
        priority = priority,
        userChangeAssignee = userChangeAssignee,
        organization = organization,
        shortMessage = shortMessage,
        externalId = externalId,
        relatedAssignment = relatedAssignment,
        meanSource = meanSource,
        id = id
    )
}

private fun List<NetworkTaskAssignee>?.toTaskAssigneeEntities(taskId: String) = this
    ?.map {
        TaskAssigneeEntity(
            taskId = taskId,
            employeeId = it.employeeId,
            complete = it.complete
        )
    } ?: emptyList()

private fun List<NetworkChecklistTask>?.toChecklistEntities(taskId: String): ChecklistEntities {
    val checklistTasks = mutableListOf<ChecklistTaskEntity>()
    val responsibles = mutableListOf<ChecklistTaskResponsibleEntity>()

    this?.forEach { checklist ->
        checklistTasks.add(
            ChecklistTaskEntity(
                id = checklist.id,
                taskId = taskId,
                title = checklist.title,
                done = checklist.done,
                placement = checklist.placement,
                deadline = checklist.deadline
            )
        )
        checklist.responsibles?.forEach { employeeId ->
            responsibles.add(
                ChecklistTaskResponsibleEntity(
                    checklistTaskId = checklist.id,
                    employeeId = employeeId
                )
            )
        }
    }

    return ChecklistEntities(checklistTasks, responsibles)
}

// Task -> TaskEntity
fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id!!,
    flowId = flowId?.toString(),
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate?.toString(),
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = lifeAreaId?.toString()
)

// Task -> TaskPayloadEntity
fun Task.toPayloadEntity() = payload?.let {
    TaskPayloadEntity(
        taskId = id!!,
        title = it.title,
        source = it.source,
        onMainPage = it.onMainPage,
        deadline = it.deadline?.toString(),
        lifeArea = it.lifeArea,
        lifeAreaId = it.lifeAreaId?.toString(),
        subtitle = it.subtitle,
        userEdit = it.userEdit,
        important = it.important,
        messageId = it.messageId,
        fullMessage = it.fullMessage,
        description = it.description,
        priority = it.priority,
        userChangeAssignee = it.userChangeAssignee,
        organization = it.organization,
        shortMessage = it.shortMessage,
        externalId = it.externalId,
        relatedAssignment = it.relatedAssignment,
        meanSource = it.meanSource,
        id = it.id
    )
}

// Task -> List<TaskAssigneeEntity>
fun Task.toAssigneeEntities(): List<TaskAssigneeEntity> =
    assignees.orEmpty().map { assignee ->
        TaskAssigneeEntity(
            taskId = id!!,
            employeeId = assignee.employeeId,
            complete = assignee.complete
        )
    }

data class LifeAreaEntities(
    val lifeArea: LifeAreaEntity,
    val sharedInfo: LifeAreaSharedInfoEntity?,
    val sharedRecipients: List<LifeAreaSharedInfoRecipientEntity>,
    val flows: List<FlowEntities>
)

data class FlowEntities(
    val flow: LifeFlowEntity,
    val tasks: List<TaskEntities>
)

data class TaskEntities(
    val task: TaskEntity,
    val payload: TaskPayloadEntity?,
    val assignees: List<TaskAssigneeEntity>?,
    val checklists: ChecklistEntities?
)

data class ChecklistEntities(
    val checklistTasks: List<ChecklistTaskEntity>,
    val responsibles: List<ChecklistTaskResponsibleEntity>
)