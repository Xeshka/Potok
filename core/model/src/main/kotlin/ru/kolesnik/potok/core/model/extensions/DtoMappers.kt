package ru.kolesnik.potok.core.model.extensions

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

// LifeArea mappers
fun LifeAreaDTO.toDomain(): LifeArea {
    return LifeArea(
        id = id,
        title = title,
        style = style,
        tagsId = tagsId,
        placement = placement,
        isDefault = isDefault,
        isTheme = isTheme,
        shared = sharedInfo?.toDomain(id)
    )
}

fun LifeAreaSharedInfo.toDomain(areaId: UUID): LifeAreaSharedInfo {
    return LifeAreaSharedInfo(
        areaId = areaId,
        owner = owner,
        recipients = recipients
    )
}

// LifeFlow mappers
fun LifeFlowDTO.toDomain(): LifeFlow {
    return LifeFlow(
        id = id.toString(),
        areaId = areaId.toString(),
        title = title,
        style = style,
        placement = placement,
        status = status ?: FlowStatus.NEW
    )
}

// Task mappers
fun TaskRs.toDomain(): TaskMain {
    return TaskMain(
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
        flowId = flowId
    )
}

fun TaskRs.toTaskDomain(): Task {
    return Task(
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
        assignees = assignees?.map { it.toDomain() },
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        checkList = checkList?.map { it.toDomain() },
        lifeAreaId = payload.lifeAreaId,
        flowId = flowId
    )
}

fun TaskPayload.toDomain(): TaskPayload {
    return TaskPayload(
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
}

fun TaskAssigneeRs.toDomain(): TaskAssignee {
    return TaskAssignee(
        employeeId = employeeId,
        complete = complete
    )
}

fun ChecklistTaskDTO.toDomain(): ChecklistTask {
    return ChecklistTask(
        id = id,
        title = title,
        done = done ?: false,
        placement = placement ?: 0,
        responsibles = responsibles,
        deadline = deadline
    )
}