package ru.kolesnik.potok.core.model.extensions

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.TaskRs
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeFlow
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import java.util.UUID

// DTO to Domain model mappers
fun LifeAreaDTO.toDomain(): LifeArea = LifeArea(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    shared = sharedInfo?.let {
        LifeAreaSharedInfo(
            areaId = id,
            owner = it.owner,
            recipients = it.recipients
        )
    }
)

fun LifeFlowDTO.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status ?: FlowStatus.NEW
)

fun TaskRs.toDomain(): TaskMain = TaskMain(
    id = id,
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { 
        TaskAssignee(
            employeeId = it.employeeId,
            complete = it.complete
        )
    },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = payload.lifeAreaId,
    flowId = flowId?.let { UUID.fromString(it.toString()) }
)

fun TaskRs.toFullDomain(): Task = Task(
    id = id,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.toDomain(),
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { 
        TaskAssignee(
            employeeId = it.employeeId,
            complete = it.complete
        )
    },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    checkList = checkList?.map {
        ChecklistTask(
            id = it.id,
            title = it.title,
            done = it.done ?: false,
            placement = it.placement ?: 0,
            responsibles = it.responsibles,
            deadline = it.deadline
        )
    },
    lifeAreaId = payload.lifeAreaId,
    flowId = flowId?.let { UUID.fromString(it.toString()) }
)

fun ru.kolesnik.potok.core.network.model.api.TaskPayload.toDomain(): TaskPayload = TaskPayload(
    title = title,
    source = source,
    onMainPage = onMainPage,
    deadline = deadline,
    lifeArea = lifeArea,
    lifeAreaId = lifeAreaId,
    subtitle = subtitle,
    userEdit = userEdit,
    assignees = assignees,
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

// Network models to Domain models
fun NetworkLifeArea.toDomain(): LifeArea = LifeArea(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    shared = sharedInfo?.let {
        LifeAreaSharedInfo(
            areaId = id,
            owner = it.owner,
            recipients = it.recipients
        )
    }
)

fun NetworkLifeFlow.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status?.let { FlowStatus.valueOf(it) } ?: FlowStatus.NEW
)

fun NetworkTask.toDomain(): TaskMain = TaskMain(
    id = id,
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { 
        TaskAssignee(
            employeeId = it.employeeId,
            complete = it.complete
        )
    },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = payload.lifeAreaId,
    flowId = cardId
)

fun NetworkTask.toFullDomain(): Task = Task(
    id = id,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.toDomain(),
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { 
        TaskAssignee(
            employeeId = it.employeeId,
            complete = it.complete
        )
    },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    checkList = checkList?.map {
        ChecklistTask(
            id = it.id,
            title = it.title,
            done = it.done ?: false,
            placement = it.placement ?: 0,
            responsibles = it.responsibles,
            deadline = it.deadline
        )
    },
    lifeAreaId = payload.lifeAreaId,
    flowId = cardId
)

fun ru.kolesnik.potok.core.network.model.potok.NetworkTaskPayload.toDomain(): TaskPayload = TaskPayload(
    title = title,
    source = source,
    onMainPage = onMainPage,
    deadline = deadline,
    lifeArea = lifeArea,
    lifeAreaId = lifeAreaId,
    subtitle = subtitle,
    userEdit = userEdit,
    assignees = assignees,
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