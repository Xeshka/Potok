package ru.kolesnik.potok.core.model.extensions

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.TaskAssigneeRs
import ru.kolesnik.potok.core.network.model.api.TaskRs
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeFlow
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.NetworkTaskAssignee
import java.util.UUID

// LifeArea mappers
fun LifeAreaDTO.toDomain(): LifeArea = LifeArea(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    shared = sharedInfo
)

fun NetworkLifeArea.toDomain(): LifeArea = LifeArea(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    shared = sharedInfo
)

// LifeFlow mappers
fun LifeFlowDTO.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status ?: FlowStatus.NEW
)

fun NetworkLifeFlow.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status ?: FlowStatus.NEW
)

// Task mappers
fun TaskRs.toDomain(): Task = Task(
    id = id,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { it.toDomain() },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    checkList = checkList?.map { it.toDomain() },
    lifeAreaId = null,
    flowId = null
)

fun NetworkTask.toDomain(): Task = Task(
    id = id,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { it.toDomain() },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    checkList = checkList?.map { it.toDomain() },
    lifeAreaId = null,
    flowId = null
)

fun TaskRs.toTaskMain(): TaskMain = TaskMain(
    id = id,
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { it.toDomain() },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = payload.lifeAreaId,
    flowId = null
)

fun NetworkTask.toTaskMain(): TaskMain = TaskMain(
    id = id,
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees?.map { it.toDomain() },
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = payload.lifeAreaId,
    flowId = null
)

// Assignee mappers
fun TaskAssigneeRs.toDomain(): TaskAssignee = TaskAssignee(
    employeeId = employeeId,
    complete = complete
)

fun NetworkTaskAssignee.toDomain(): TaskAssignee = TaskAssignee(
    employeeId = employeeId,
    complete = complete
)

// Checklist mappers
fun ru.kolesnik.potok.core.network.model.api.ChecklistTaskDTO.toDomain(): ChecklistTask = ChecklistTask(
    id = id,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
)

fun ru.kolesnik.potok.core.network.model.potok.NetworkChecklistTask.toDomain(): ChecklistTask = ChecklistTask(
    id = id,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
)