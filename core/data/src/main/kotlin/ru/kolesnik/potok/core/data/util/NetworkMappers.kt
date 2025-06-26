package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.*

/**
 * ✅ Маппинг Network DTO -> Domain Models
 * Этот файл содержит всю логику преобразования сетевых моделей в доменные
 */

// LifeArea mapping
fun LifeAreaDTO.toDomainModel(): LifeArea {
    return LifeArea(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId?.toInt(),
        placement = this.placement,
        isDefault = this.isDefault,
        isTheme = this.isTheme,
        shared = this.sharedInfo?.toDomainModel()
    )
}

fun LifeAreaSharedInfo.toDomainModel(): ru.kolesnik.potok.core.model.LifeAreaSharedInfo {
    return ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
        areaId = java.util.UUID.randomUUID(), // Будет переопределено
        owner = this.owner,
        recipients = this.recipients
    )
}

// LifeFlow mapping
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

// Task mapping
fun TaskRs.toDomainModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate,
        payload = this.payload.toDomainModel(),
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = this.assignees?.map { it.toDomainModel() } ?: emptyList(),
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        checkList = this.checkList?.map { it.toDomainModel() },
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null // Будет установлено отдельно
    )
}

fun TaskPayload.toDomainModel(): ru.kolesnik.potok.core.model.TaskPayload {
    return ru.kolesnik.potok.core.model.TaskPayload(
        title = this.title,
        source = this.source,
        onMainPage = this.onMainPage,
        deadline = this.deadline,
        lifeArea = this.lifeArea,
        lifeAreaId = this.lifeAreaId,
        subtitle = this.subtitle,
        userEdit = this.userEdit,
        assignees = this.assignees,
        important = this.important,
        messageId = this.messageId,
        fullMessage = this.fullMessage,
        description = this.description,
        priority = this.priority,
        userChangeAssignee = this.userChangeAssignee,
        organization = this.organization,
        shortMessage = this.shortMessage,
        externalId = this.externalId,
        relatedAssignment = this.relatedAssignment,
        meanSource = this.meanSource,
        id = this.id
    )
}

fun TaskAssigneeRs.toDomainModel(): TaskAssignee {
    return TaskAssignee(
        employeeId = this.employeeId,
        complete = this.complete
    )
}

fun ChecklistTaskDTO.toDomainModel(): ChecklistTask {
    return ChecklistTask(
        id = this.id,
        title = this.title,
        done = this.done ?: false,
        placement = this.placement ?: 0,
        responsibles = this.responsibles ?: emptyList(),
        deadline = this.deadline
    )
}

fun TaskCommentDTO.toDomainModel(): TaskComment {
    return TaskComment(
        id = this.id,
        parentCommentId = this.parentCommentId,
        owner = this.owner,
        text = this.text,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

// Employee mapping
fun ru.kolesnik.potok.core.network.model.employee.EmployeeResponse.toDomainModel(): Employee {
    return Employee(
        employeeNumber = this.employeeNumber,
        timezone = this.timezone,
        terBank = this.terBank,
        employeeId = this.employeeId,
        lastName = this.lastName,
        firstName = this.firstName,
        middleName = this.middleName,
        position = this.position,
        mainEmail = this.mainEmail,
        avatar = this.avatar
    )
}

// Domain Model -> Network DTO mapping (для создания/обновления)
fun Task.toNetworkCreateRequest(): NetworkCreateTask {
    return NetworkCreateTask(
        lifeAreaId = this.lifeAreaId,
        flowId = this.flowId,
        payload = this.payload?.toNetworkPayload() ?: NetworkTaskPayload()
    )
}

fun ru.kolesnik.potok.core.model.TaskPayload.toNetworkPayload(): NetworkTaskPayload {
    return NetworkTaskPayload(
        title = this.title,
        source = this.source,
        onMainPage = this.onMainPage,
        deadline = this.deadline,
        lifeArea = this.lifeArea,
        lifeAreaId = this.lifeAreaId,
        subtitle = this.subtitle,
        userEdit = this.userEdit,
        assignees = this.assignees,
        important = this.important,
        messageId = this.messageId,
        fullMessage = this.fullMessage,
        description = this.description,
        priority = this.priority,
        userChangeAssignee = this.userChangeAssignee,
        organization = this.organization,
        shortMessage = this.shortMessage,
        externalId = this.externalId,
        relatedAssignment = this.relatedAssignment,
        meanSource = this.meanSource,
        id = this.id
    )
}