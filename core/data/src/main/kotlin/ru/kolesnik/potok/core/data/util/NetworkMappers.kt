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
        shared = this.sharedInfo?.let { sharedInfo ->
            ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
                areaId = this.id, // Используем ID области жизни
                owner = sharedInfo.owner,
                recipients = sharedInfo.recipients
            )
        }
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
        payload = ru.kolesnik.potok.core.model.TaskPayload(
            title = this.payload.title,
            source = this.payload.source,
            onMainPage = this.payload.onMainPage,
            deadline = this.payload.deadline,
            lifeArea = this.payload.lifeArea,
            lifeAreaId = this.payload.lifeAreaId,
            subtitle = this.payload.subtitle,
            userEdit = this.payload.userEdit,
            assignees = this.payload.assignees,
            important = this.payload.important,
            messageId = this.payload.messageId,
            fullMessage = this.payload.fullMessage,
            description = this.payload.description,
            priority = this.payload.priority,
            userChangeAssignee = this.payload.userChangeAssignee,
            organization = this.payload.organization,
            shortMessage = this.payload.shortMessage,
            externalId = this.payload.externalId,
            relatedAssignment = this.payload.relatedAssignment,
            meanSource = this.payload.meanSource,
            id = this.payload.id
        ),
        internalId = this.internalId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        assignees = this.assignees?.map { assignee ->
            TaskAssignee(
                employeeId = assignee.employeeId,
                complete = assignee.complete
            )
        } ?: emptyList(),
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount,
        checkList = this.checkList?.map { checklistItem ->
            ChecklistTask(
                id = checklistItem.id,
                title = checklistItem.title,
                done = checklistItem.done ?: false,
                placement = checklistItem.placement ?: 0,
                responsibles = checklistItem.responsibles ?: emptyList(),
                deadline = checklistItem.deadline
            )
        },
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null // Будет установлено отдельно
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
        payload = NetworkTaskPayload(
            title = this.payload?.title,
            source = this.payload?.source,
            onMainPage = this.payload?.onMainPage,
            deadline = this.payload?.deadline,
            lifeArea = this.payload?.lifeArea,
            lifeAreaId = this.payload?.lifeAreaId,
            subtitle = this.payload?.subtitle,
            userEdit = this.payload?.userEdit,
            assignees = this.payload?.assignees,
            important = this.payload?.important,
            messageId = this.payload?.messageId,
            fullMessage = this.payload?.fullMessage,
            description = this.payload?.description,
            priority = this.payload?.priority,
            userChangeAssignee = this.payload?.userChangeAssignee,
            organization = this.payload?.organization,
            shortMessage = this.payload?.shortMessage,
            externalId = this.payload?.externalId,
            relatedAssignment = this.payload?.relatedAssignment,
            meanSource = this.payload?.meanSource,
            id = this.payload?.id
        )
    )
}