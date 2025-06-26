package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.*
import java.util.UUID

// DTO to Entity mappers
fun LifeAreaDTO.toEntity(): LifeAreaEntity = LifeAreaEntity(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    sharedInfo = sharedInfo?.toModel(),
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

fun LifeFlowDTO.toEntity(): LifeFlowEntity = LifeFlowEntity(
    id = id,
    areaId = areaId,
    title = title,
    style = style,
    placement = placement,
    status = status?.toModel()
)

fun TaskRs.toEntity(): TaskEntity = TaskEntity(
    cardId = UUID.fromString(id),
    externalId = payload.externalId,
    internalId = internalId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.toModel(),
    lifeAreaId = null, // Will be set separately
    flowId = null, // Will be set separately
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    deletedAt = null
)

fun ChecklistTaskDTO.toEntity(taskId: UUID): ChecklistTaskEntity = ChecklistTaskEntity(
    id = id,
    taskCardId = taskId,
    title = title,
    done = done ?: false,
    placement = placement,
    responsibles = responsibles,
    deadline = deadline
)

fun TaskAssigneeRs.toEntity(taskId: UUID): TaskAssigneeEntity = TaskAssigneeEntity(
    taskCardId = taskId,
    employeeId = employeeId,
    complete = complete
)

fun TaskCommentDTO.toEntity(taskId: UUID): TaskCommentEntity = TaskCommentEntity(
    id = id,
    taskCardId = taskId,
    parentCommentId = parentCommentId,
    owner = owner,
    text = text,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Network to Entity mappers
fun NetworkLifeArea.toEntity(): LifeAreaEntity = LifeAreaEntity(
    id = UUID.fromString(id),
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    sharedInfo = sharedInfo?.toModel(),
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

fun NetworkLifeFlow.toEntity(): LifeFlowEntity = LifeFlowEntity(
    id = UUID.fromString(id),
    areaId = UUID.fromString(areaId),
    title = title,
    style = style,
    placement = placement,
    status = status?.toModel()
)

fun NetworkTask.toEntity(): TaskEntity = TaskEntity(
    cardId = UUID.fromString(id),
    externalId = payload?.externalId,
    internalId = internalId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload?.toModel() ?: TaskPayload(),
    lifeAreaId = null, // Will be set from context
    flowId = null, // Will be set from context
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    deletedAt = null
)

// Entity to Domain mappers
fun LifeAreaEntity.toDomain(): LifeArea = LifeArea(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    isTheme = isTheme,
    shared = sharedInfo
)

fun LifeFlowEntity.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status ?: FlowStatus.NEW
)

fun TaskEntity.toDomain(): Task = Task(
    id = externalId,
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
    lifeAreaId = lifeAreaId,
    flowId = flowId
)

fun TaskEntity.toTaskMain(): TaskMain = TaskMain(
    id = externalId ?: cardId.toString(),
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = lifeAreaId,
    flowId = flowId
)

fun ChecklistTaskEntity.toDomain(): ChecklistTask = ChecklistTask(
    id = id,
    title = title,
    done = done,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
)

fun TaskAssigneeEntity.toDomain(): TaskAssignee = TaskAssignee(
    employeeId = employeeId,
    complete = complete
)

fun TaskCommentEntity.toDomain(): TaskComment = TaskComment(
    id = id,
    parentCommentId = parentCommentId,
    owner = owner,
    text = text,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Helper conversion methods
private fun LifeAreaSharedInfo.toModel(): ru.kolesnik.potok.core.model.LifeAreaSharedInfo = 
    ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
        areaId = UUID.randomUUID(), // This should be set from context
        owner = owner,
        recipients = recipients
    )

private fun FlowStatus.toModel(): ru.kolesnik.potok.core.model.FlowStatus = when (this) {
    FlowStatus.NEW -> ru.kolesnik.potok.core.model.FlowStatus.NEW
    FlowStatus.IN_PROGRESS -> ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS
    FlowStatus.COMPLETED -> ru.kolesnik.potok.core.model.FlowStatus.COMPLETED
    FlowStatus.CUSTOM -> ru.kolesnik.potok.core.model.FlowStatus.CUSTOM
}

private fun TaskPayload.toModel(): ru.kolesnik.potok.core.model.TaskPayload = 
    ru.kolesnik.potok.core.model.TaskPayload(
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

private fun NetworkTaskPayload.toModel(): ru.kolesnik.potok.core.model.TaskPayload = 
    ru.kolesnik.potok.core.model.TaskPayload(
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

private fun NetworkLifeAreaSharedInfo.toModel(): ru.kolesnik.potok.core.model.LifeAreaSharedInfo = 
    ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
        areaId = UUID.randomUUID(), // This should be set from context
        owner = owner,
        recipients = recipients
    )