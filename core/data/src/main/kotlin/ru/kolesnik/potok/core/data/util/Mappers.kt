package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
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
    sharedInfo = sharedInfo,
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

fun LifeFlowDTO.toEntity(): LifeFlowEntity = LifeFlowEntity(
    id = id,
    areaId = areaId,
    title = title,
    style = style,
    placement = placement,
    status = status
)

fun TaskRs.toEntity(): TaskEntity = TaskEntity(
    cardId = UUID.fromString(id),
    externalId = id,
    internalId = internalId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.toModelPayload(),
    lifeAreaId = payload.lifeAreaId,
    flowId = null, // Will be set separately
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount
)

fun TaskAssigneeRs.toEntity(taskId: UUID): TaskAssigneeEntity = TaskAssigneeEntity(
    taskCardId = taskId,
    employeeId = employeeId,
    complete = complete
)

fun ChecklistTaskDTO.toEntity(taskId: UUID): ChecklistTaskEntity = ChecklistTaskEntity(
    id = id,
    taskCardId = taskId,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles,
    deadline = deadline
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
    sharedInfo = null, // Convert if needed
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

fun NetworkTask.toEntity(): TaskEntity = TaskEntity(
    cardId = UUID.fromString(id),
    externalId = id,
    internalId = internalId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.toModelPayload(),
    lifeAreaId = lifeAreaId?.let { UUID.fromString(it) },
    flowId = flowId?.let { UUID.fromString(it) },
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    commentCount = commentCount,
    attachmentCount = attachmentCount
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
    shared = sharedInfo?.toDomain()
)

fun LifeFlowEntity.toDomain(): LifeFlow = LifeFlow(
    id = id.toString(),
    areaId = areaId.toString(),
    title = title,
    style = style,
    placement = placement,
    status = status ?: ru.kolesnik.potok.core.model.FlowStatus.NEW
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
    lifeAreaId = lifeAreaId,
    flowId = flowId
)

fun TaskAssigneeEntity.toDomain(): TaskAssignee = TaskAssignee(
    employeeId = employeeId,
    complete = complete
)

fun ChecklistTaskEntity.toDomain(): ChecklistTask = ChecklistTask(
    id = id,
    title = title,
    done = done ?: false,
    placement = placement ?: 0,
    responsibles = responsibles ?: emptyList(),
    deadline = deadline
)

fun TaskCommentEntity.toDomain(): TaskComment = TaskComment(
    id = id,
    parentCommentId = parentCommentId,
    owner = owner,
    text = text,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Helper mappers
fun EmployeeResponse.toDomain(): Employee = Employee(
    employeeNumber = employeeNumber,
    timezone = timezone,
    terBank = terBank,
    employeeId = employeeId,
    lastName = lastName,
    firstName = firstName,
    middleName = middleName,
    position = position,
    mainEmail = mainEmail,
    avatar = avatar
)

fun LifeAreaSharedInfo.toDomain(): ru.kolesnik.potok.core.model.LifeAreaSharedInfo = 
    ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
        areaId = UUID.randomUUID(), // This should be set properly
        owner = owner,
        recipients = recipients
    )

fun ru.kolesnik.potok.core.network.model.api.TaskPayload.toModelPayload(): ru.kolesnik.potok.core.model.TaskPayload = 
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

fun NetworkTaskPayload.toModelPayload(): ru.kolesnik.potok.core.model.TaskPayload = 
    ru.kolesnik.potok.core.model.TaskPayload(
        title = title,
        source = source,
        onMainPage = onMainPage,
        deadline = deadline,
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId?.let { UUID.fromString(it) },
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

// Domain to DTO mappers (for requests)
fun Task.toTaskRq(): TaskRq = TaskRq(
    lifeAreaId = lifeAreaId,
    flowId = flowId,
    payload = payload?.toApiPayload() ?: ru.kolesnik.potok.core.network.model.api.TaskPayload()
)

fun ru.kolesnik.potok.core.model.TaskPayload.toApiPayload(): ru.kolesnik.potok.core.network.model.api.TaskPayload = 
    ru.kolesnik.potok.core.network.model.api.TaskPayload(
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