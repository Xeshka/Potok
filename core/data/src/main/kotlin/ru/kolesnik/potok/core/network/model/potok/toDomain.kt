package ru.kolesnik.potok.core.network.model.potok

import ru.kolesnik.potok.core.database.model.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.model.ChecklistTaskResponsibleEntity
import ru.kolesnik.potok.core.database.model.LifeAreaEntity
import ru.kolesnik.potok.core.database.model.LifeAreaId
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoRecipientEntity
import ru.kolesnik.potok.core.database.model.LifeFlowEntity
import ru.kolesnik.potok.core.database.model.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.model.TaskEntity
import ru.kolesnik.potok.core.database.model.TaskPayloadEntity
import ru.kolesnik.potok.core.model.ChecklistTask
import ru.kolesnik.potok.core.model.EmployeeId
import ru.kolesnik.potok.core.model.FlowStatus
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.LifeAreaSharedInfo
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.model.TaskPayload
import java.time.OffsetDateTime
import java.util.UUID

fun LifeAreaEntity.toDomain(
    sharedInfoEntity: LifeAreaSharedInfoEntity?,
    recipientEntities: List<LifeAreaSharedInfoRecipientEntity>?
): LifeArea {
    return LifeArea(
        id = UUID.fromString(id),
        title = title,
        style = style,
        tagsId = tagsId,
        placement = placement,
        isDefault = isDefault,
        shared = sharedInfoEntity?.toDomain(id, recipientEntities!!),
        isTheme = isTheme,
    )
}

fun LifeAreaSharedInfoEntity.toDomain(
    areaId: LifeAreaId,
    recipients: List<LifeAreaSharedInfoRecipientEntity>
): LifeAreaSharedInfo {
    return LifeAreaSharedInfo(
        areaId = UUID.fromString(areaId),
        owner = owner,
        recipients = recipients.map { it.employeeId }
    )
}

fun TaskEntity.toDomainModel(
    payload: TaskPayloadEntity?,
    assignees: List<TaskAssigneeEntity>,
    checklistTasks: List<ChecklistTaskEntity>,
    checklistResponsibles: Map<String, List<ChecklistTaskResponsibleEntity>>
): Task {
    return Task(
        id = id,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = runCatching { OffsetDateTime.parse(creationDate) }.getOrNull(),
        payload = payload?.toDomainModel(assignees.map { it.employeeId }),
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = assignees.toDomainModel(),
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        checkList = checklistTasks.toDomainModel(checklistResponsibles),
        flowId = UUID.fromString(flowId),
        lifeAreaId = UUID.fromString(lifeAreaId)
    )
}

fun TaskEntity.toDomainModel(
    assignees: List<TaskAssigneeEntity>,
    deadline: String?
): TaskMain {
    return TaskMain(
        id = id,
        title = title,
        source = source,
        taskOwner = taskOwner,
        creationDate = runCatching { OffsetDateTime.parse(creationDate) }.getOrNull(),
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = assignees.toDomainModel(),
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        flowId = UUID.fromString(flowId),
        lifeAreaId = UUID.fromString(lifeAreaId),
        deadline = runCatching { OffsetDateTime.parse(deadline) }.getOrNull(),
    )
}

fun TaskPayloadEntity?.toDomainModel(
    assignees: List<EmployeeId>
): TaskPayload? = this?.let {
    TaskPayload(
        title = title,
        source = source,
        onMainPage = onMainPage,
        deadline = runCatching { OffsetDateTime.parse(deadline) }.getOrNull(),
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId?.let { UUID.fromString(lifeAreaId) },
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
        id = id,
        assignees = assignees
    )
}

fun List<TaskAssigneeEntity>.toDomainModel(): List<TaskAssignee> =
    map { it.toDomainModel() }

fun TaskAssigneeEntity.toDomainModel(): TaskAssignee =
    TaskAssignee(
        employeeId = employeeId,
        complete = complete
    )

fun List<ChecklistTaskEntity>.toDomainModel(
    responsiblesMap: Map<String, List<ChecklistTaskResponsibleEntity>>
): List<ChecklistTask> = map { checklistTask ->
    ChecklistTask(
        id = UUID.fromString(checklistTask.id),
        title = checklistTask.title,
        done = checklistTask.done,
        placement = checklistTask.placement,
        responsibles = responsiblesMap[checklistTask.id]
            ?.map { it.employeeId } ?: emptyList(),
        deadline = runCatching { OffsetDateTime.parse(checklistTask.deadline) }.getOrNull(),
    )
}

fun List<LifeFlowEntity>.toFlowDomainModel(): List<LifeFlow> =
    map {
        LifeFlow(
            it.id,
            it.areaId,
            it.title,
            it.style,
            it.placement,
            FlowStatus.valueOf(it.status),
        )
    }

fun LifeFlowEntity.toFlowDomainModel(): LifeFlow =
    LifeFlow(
        id,
        areaId,
        title,
        style,
        placement,
        FlowStatus.valueOf(status),
    )

fun TaskEntity.toTaskDomain(
    payload: TaskPayload?,
    assignees: List<TaskAssignee>
): Task = Task(
    id = id,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = runCatching { OffsetDateTime.parse(creationDate) }.getOrNull(),
    payload = payload,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = UUID.fromString(lifeAreaId),
    flowId = UUID.fromString(flowId)
)

// TaskPayloadEntity -> TaskPayload
fun TaskPayloadEntity.toDomain() = TaskPayload(
    title = title,
    description = description,
    important = important,
    deadline = runCatching { OffsetDateTime.parse(deadline) }.getOrNull(),
    // остальные поля
)
