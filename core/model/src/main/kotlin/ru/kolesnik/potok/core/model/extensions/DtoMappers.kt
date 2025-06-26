package ru.kolesnik.potok.core.model.extensions

import ru.kolesnik.potok.core.database.entitys.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import ru.kolesnik.potok.core.database.entitys.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.entitys.TaskEntity
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.model.LifeFlow
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.model.TaskPayload
import ru.kolesnik.potok.core.network.model.api.ChecklistTaskDTO
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.api.LifeFlowDTO
import ru.kolesnik.potok.core.network.model.api.TaskAssigneeRs
import ru.kolesnik.potok.core.network.model.api.TaskRq
import ru.kolesnik.potok.core.network.model.api.TaskRs
import java.util.UUID

// DTO to Entity mappers
fun LifeAreaDTO.toEntity(): LifeAreaEntity = LifeAreaEntity(
    id = id,
    title = title,
    style = style,
    tagsId = tagsId,
    placement = placement,
    isDefault = isDefault,
    sharedInfo = sharedInfo?.let {
        ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
            areaId = id,
            owner = it.owner,
            recipients = it.recipients
        )
    },
    isTheme = isTheme,
    onlyPersonal = onlyPersonal
)

fun LifeFlowDTO.toEntity(): LifeFlowEntity = LifeFlowEntity(
    id = id,
    areaId = areaId,
    title = title,
    style = style,
    placement = placement,
    status = status?.let { 
        ru.kolesnik.potok.core.model.FlowStatus.valueOf(it.name)
    }
)

fun TaskRs.toEntity(): TaskEntity = TaskEntity(
    cardId = cardId,
    externalId = id,
    internalId = internalId,
    title = title,
    subtitle = subtitle,
    mainOrder = mainOrder,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    payload = payload.let { 
        TaskPayload(
            title = it.title,
            source = it.source,
            onMainPage = it.onMainPage,
            deadline = it.deadline,
            lifeArea = it.lifeArea,
            lifeAreaId = it.lifeAreaId,
            subtitle = it.subtitle,
            userEdit = it.userEdit,
            assignees = it.assignees,
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
    },
    lifeAreaId = payload.lifeAreaId,
    flowId = null, // Будет установлено позже
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
    status = status ?: ru.kolesnik.potok.core.model.FlowStatus.NEW
)

fun TaskEntity.toDomain(assignees: List<TaskAssignee> = emptyList()): Task = Task(
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
    assignees = assignees,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = lifeAreaId,
    flowId = flowId
)

fun TaskEntity.toDomain(assignees: List<TaskAssignee> = emptyList()): TaskMain = TaskMain(
    id = externalId ?: cardId.toString(),
    title = title,
    source = source,
    taskOwner = taskOwner,
    creationDate = creationDate,
    deadline = payload.deadline,
    internalId = internalId,
    lifeAreaPlacement = lifeAreaPlacement,
    flowPlacement = flowPlacement,
    assignees = assignees,
    commentCount = commentCount,
    attachmentCount = attachmentCount,
    lifeAreaId = lifeAreaId,
    flowId = flowId
)

fun TaskAssigneeEntity.toDomain(): TaskAssignee = TaskAssignee(
    employeeId = employeeId,
    complete = complete
)

// Domain to Request mappers
fun Task.toRequest(): TaskRq = TaskRq(
    lifeAreaId = lifeAreaId,
    flowId = flowId,
    payload = payload ?: TaskPayload(
        title = title,
        description = "",
        important = false
    )
)