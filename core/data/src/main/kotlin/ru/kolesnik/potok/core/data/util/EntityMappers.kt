package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*

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