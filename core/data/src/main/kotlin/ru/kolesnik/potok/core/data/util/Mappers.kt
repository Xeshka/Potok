package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import java.time.OffsetDateTime
import java.util.*

// ✅ Entity -> Model mappers (для чтения из базы данных)
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

// ✅ Model -> Entity mappers (для записи в базу данных)
fun LifeArea.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId?.toLong(),
        placement = this.placement,
        isDefault = this.isDefault,
        sharedInfo = this.shared,
        isTheme = this.isTheme,
        onlyPersonal = true // Значение по умолчанию
    )
}

fun LifeFlow.toEntity(): LifeFlowEntity {
    return LifeFlowEntity(
        id = UUID.fromString(this.id),
        areaId = UUID.fromString(this.areaId),
        title = this.title,
        style = this.style,
        placement = this.placement,
        status = when (this.status) {
            ru.kolesnik.potok.core.model.FlowStatus.NEW -> ru.kolesnik.potok.core.network.model.api.FlowStatus.NEW
            ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS -> ru.kolesnik.potok.core.network.model.api.FlowStatus.IN_PROGRESS
            ru.kolesnik.potok.core.model.FlowStatus.COMPLETED -> ru.kolesnik.potok.core.network.model.api.FlowStatus.COMPLETED
            ru.kolesnik.potok.core.model.FlowStatus.CUSTOM -> ru.kolesnik.potok.core.network.model.api.FlowStatus.CUSTOM
            ru.kolesnik.potok.core.model.FlowStatus.COMPLETE -> ru.kolesnik.potok.core.network.model.api.FlowStatus.COMPLETED
        }
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        cardId = UUID.randomUUID(), // Будет переопределено при необходимости
        externalId = this.id,
        internalId = this.internalId,
        title = this.title,
        subtitle = this.subtitle,
        mainOrder = this.mainOrder,
        source = this.source,
        taskOwner = this.taskOwner,
        creationDate = this.creationDate ?: OffsetDateTime.now(),
        payload = this.payload ?: TaskPayload(),
        lifeAreaId = this.lifeAreaId,
        flowId = this.flowId,
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount
    )
}

fun TaskComment.toEntity(): TaskCommentEntity {
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

fun ChecklistTask.toEntity(): ChecklistTaskEntity {
    return ChecklistTaskEntity(
        id = this.id,
        taskCardId = UUID.randomUUID(), // Будет переопределено при использовании
        title = this.title,
        done = this.done,
        placement = this.placement,
        responsibles = this.responsibles,
        deadline = this.deadline
    )
}

// ✅ Network DTO -> Entity mappers (прямое преобразование для оптимизации)
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

// Model -> DTO mappers для создания запросов
fun TaskComment.toCreateRequest(): ru.kolesnik.potok.core.network.model.api.TaskCommentRq {
    return ru.kolesnik.potok.core.network.model.api.TaskCommentRq(
        text = this.text,
        parentCommentId = this.parentCommentId
    )
}

fun ChecklistTask.toCreateRequest(): ru.kolesnik.potok.core.network.model.api.ChecklistTaskTitleRq {
    return ru.kolesnik.potok.core.network.model.api.ChecklistTaskTitleRq(title = this.title)
}