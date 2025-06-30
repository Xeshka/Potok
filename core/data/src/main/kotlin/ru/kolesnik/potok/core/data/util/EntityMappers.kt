package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.model.TaskPayload

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
        payload = this.payload?.toModel(),
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
        deadline = this.payload?.deadline,
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

fun TaskPayload.toEntity(): ru.kolesnik.potok.core.database.entitys.TaskPayload {
    return ru.kolesnik.potok.core.database.entitys.TaskPayload(
        title = this.title ?: "", // Преобразуем null в пустую строку (или другое дефолтное значение)
        source = this.source,
        onMainPage = this.onMainPage ?: false, // Дефолтное значение для Boolean
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

fun ru.kolesnik.potok.core.database.entitys.TaskPayload.toModel(): TaskPayload {
    return TaskPayload(
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