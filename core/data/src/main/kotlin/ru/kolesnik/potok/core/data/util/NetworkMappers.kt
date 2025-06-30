package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.database.entitys.TaskPayload
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID

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

fun TaskRs.toEntity(lifeAreaId: UUID, flowId: UUID): TaskEntity {
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
        lifeAreaId = lifeAreaId,
        flowId = flowId,
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