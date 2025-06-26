package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.*
import java.time.OffsetDateTime
import java.util.*

// Network DTO -> Entity mappers
fun LifeAreaDTO.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId,
        placement = this.placement,
        isDefault = this.isDefault,
        sharedInfo = this.sharedInfo?.let {
            ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
                areaId = this.id,
                owner = it.owner,
                recipients = it.recipients
            )
        },
        isTheme = this.isTheme,
        onlyPersonal = this.onlyPersonal
    )
}

fun LifeFlowDTO.toEntity(): LifeFlowEntity {
    return LifeFlowEntity(
        id = this.id,
        areaId = this.areaId,
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = this.status
    )
}

fun TaskRs.toEntity(): TaskEntity {
    return TaskEntity(
        cardId = this.cardId,
        externalId = this.id,
        internalId = this.internalId,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        payload = this.payload.let { 
            ru.kolesnik.potok.core.model.TaskPayload(
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
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount
    )
}

fun TaskCommentDTO.toEntity(): TaskCommentEntity {
    return TaskCommentEntity(
        id = this.id,
        taskCardId = UUID.randomUUID(), // Будет переопределено при использовании
        parentCommentId = this.parentCommentId,
        owner = this.owner,
        text = this.text,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ChecklistTaskDTO.toEntity(): ChecklistTaskEntity {
    return ChecklistTaskEntity(
        id = this.id,
        taskCardId = UUID.randomUUID(), // Будет переопределено при использовании
        title = this.title,
        done = this.done ?: false,
        placement = this.placement ?: 0,
        responsibles = this.responsibles,
        deadline = this.deadline
    )
}

// Network models -> Entity mappers
fun NetworkLifeArea.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId,
        placement = this.placement,
        isDefault = this.isDefault,
        sharedInfo = this.sharedInfo?.let {
            ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
                areaId = this.id,
                owner = it.owner,
                recipients = it.recipients
            )
        },
        isTheme = this.isTheme,
        onlyPersonal = this.onlyPersonal
    )
}

fun NetworkLifeFlow.toEntity(): LifeFlowEntity {
    return LifeFlowEntity(
        id = this.id,
        areaId = this.areaId,
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = when (this.status) {
            "NEW" -> ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW
            "IN_PROGRESS" -> ru.kolesnik.potok.core.network.model.api.FlowStatus.IN_PROGRESS
            "COMPLETED" -> ru.kolesnik.potok.core.network.model.api.FlowStatus.COMPLETED
            "CUSTOM" -> ru.kolesnik.potok.core.network.model.api.FlowStatus.CUSTOM
            else -> ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW
        }
    )
}

fun NetworkTask.toEntity(): TaskEntity {
    return TaskEntity(
        cardId = this.cardId,
        externalId = this.id,
        internalId = this.internalId,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        payload = this.payload.let { 
            ru.kolesnik.potok.core.model.TaskPayload(
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
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount
    )
}

fun NetworkTaskAssignee.toEntity(taskCardId: UUID): TaskAssigneeEntity {
    return TaskAssigneeEntity(
        taskCardId = taskCardId,
        employeeId = this.employeeId,
        complete = this.complete
    )
}

fun NetworkChecklistTask.toEntity(taskCardId: UUID): ChecklistTaskEntity {
    return ChecklistTaskEntity(
        id = this.id,
        taskCardId = taskCardId,
        title = this.title,
        done = this.done ?: false,
        placement = this.placement ?: 0,
        responsibles = this.responsibles,
        deadline = this.deadline
    )
}

// Entity -> Model mappers
fun LifeAreaEntity.toModel(): LifeArea {
    return LifeArea(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId?.toInt(),
        placement = this.placement,
        isDefault = this.isDefault,
        isTheme = this.isTheme,
        shared = this.sharedInfo
    )
}

fun LifeFlowEntity.toModel(): LifeFlow {
    return LifeFlow(
        id = this.id.toString(),
        areaId = this.areaId.toString(),
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = when (this.status) {
            ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW -> ru.kolesnik.potok.core.model.FlowStatus.NEW
            ru.kolesnik.potok.core.network.model.api.FlowStatus.IN_PROGRESS -> ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS
            ru.kolesnik.potok.core.network.model.api.FlowStatus.COMPLETED -> ru.kolesnik.potok.core.model.FlowStatus.COMPLETED
            ru.kolesnik.potok.core.network.model.api.FlowStatus.CUSTOM -> ru.kolesnik.potok.core.model.FlowStatus.CUSTOM
            null -> ru.kolesnik.potok.core.model.FlowStatus.NEW
        }
    )
}

fun TaskEntity.toModel(): Task {
    return Task(
        id = this.externalId,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        payload = this.payload,
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = emptyList(), // Будет заполнено отдельно
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        lifeAreaId = this.lifeAreaId,
        flowId = this.flowId
    )
}

fun TaskEntity.toTaskMain(): TaskMain {
    return TaskMain(
        id = this.externalId ?: this.cardId.toString(),
        title = this.title,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        deadline = this.payload.deadline,
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = emptyList(), // Будет заполнено отдельно
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        lifeAreaId = this.lifeAreaId,
        flowId = this.flowId
    )
}

fun TaskCommentEntity.toModel(): TaskComment {
    return TaskComment(
        id = this.id,
        parentCommentId = this.parentCommentId,
        owner = this.owner,
        text = this.text,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ChecklistTaskEntity.toModel(): ChecklistTask {
    return ChecklistTask(
        id = this.id,
        title = this.title,
        done = this.done,
        placement = this.placement,
        responsibles = this.responsibles ?: emptyList(),
        deadline = this.deadline
    )
}

fun TaskAssigneeEntity.toModel(): TaskAssignee {
    return TaskAssignee(
        employeeId = this.employeeId,
        complete = this.complete
    )
}

// DTO -> Model mappers (добавляем недостающие методы toDomainModel)
fun LifeAreaDTO.toDomainModel(): LifeArea {
    return LifeArea(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId?.toInt(),
        placement = this.placement,
        isDefault = this.isDefault,
        isTheme = this.isTheme,
        shared = this.sharedInfo?.let {
            ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
                areaId = this.id,
                owner = it.owner,
                recipients = it.recipients
            )
        }
    )
}

fun LifeFlowDTO.toDomainModel(): LifeFlow {
    return LifeFlow(
        id = this.id.toString(),
        areaId = this.areaId.toString(),
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = when (this.status) {
            ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW -> ru.kolesnik.potok.core.model.FlowStatus.NEW
            ru.kolesnik.potok.core.network.model.api.FlowStatus.IN_PROGRESS -> ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS
            ru.kolesnik.potok.core.network.model.api.FlowStatus.COMPLETED -> ru.kolesnik.potok.core.model.FlowStatus.COMPLETED
            ru.kolesnik.potok.core.network.model.api.FlowStatus.CUSTOM -> ru.kolesnik.potok.core.model.FlowStatus.CUSTOM
            null -> ru.kolesnik.potok.core.model.FlowStatus.NEW
        }
    )
}

fun NetworkLifeArea.toDomainModel(): LifeArea {
    return LifeArea(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId?.toInt(),
        placement = this.placement,
        isDefault = this.isDefault,
        isTheme = this.isTheme,
        shared = this.sharedInfo?.let {
            ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
                areaId = this.id,
                owner = it.owner,
                recipients = it.recipients
            )
        }
    )
}

fun NetworkLifeFlow.toDomainModel(): LifeFlow {
    return LifeFlow(
        id = this.id.toString(),
        areaId = this.areaId.toString(),
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = when (this.status) {
            "NEW" -> ru.kolesnik.potok.core.model.FlowStatus.NEW
            "IN_PROGRESS" -> ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS
            "COMPLETED" -> ru.kolesnik.potok.core.model.FlowStatus.COMPLETED
            "CUSTOM" -> ru.kolesnik.potok.core.model.FlowStatus.CUSTOM
            else -> ru.kolesnik.potok.core.model.FlowStatus.NEW
        }
    )
}

fun NetworkTask.toDomainModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        payload = this.payload.let { 
            ru.kolesnik.potok.core.model.TaskPayload(
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
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = this.assignees?.map { 
            TaskAssignee(
                employeeId = it.employeeId,
                complete = it.complete
            )
        },
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        checkList = this.checkList?.map {
            ChecklistTask(
                id = it.id,
                title = it.title,
                done = it.done ?: false,
                placement = it.placement ?: 0,
                responsibles = it.responsibles ?: emptyList(),
                deadline = it.deadline
            )
        },
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null
    )
}

// Model -> DTO mappers для создания запросов
fun TaskComment.toCreateRequest(): TaskCommentRq {
    return TaskCommentRq(
        text = this.text,
        parentCommentId = this.parentCommentId
    )
}

fun ChecklistTask.toCreateRequest(): ChecklistTaskTitleRq {
    return ChecklistTaskTitleRq(title = this.title)
}