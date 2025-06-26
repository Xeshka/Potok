package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.network.model.potok.*
import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import java.util.UUID

// Маппинг NetworkLifeArea -> LifeAreaEntity
fun NetworkLifeArea.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        title = this.title,
        style = this.style,
        tagsId = this.tagsId,
        placement = this.placement,
        isDefault = this.isDefault,
        sharedInfo = this.sharedInfo?.let {
            LifeAreaSharedInfo(
                areaId = this.id,
                owner = it.owner,
                recipients = it.recipients
            )
        },
        isTheme = this.isTheme,
        onlyPersonal = this.onlyPersonal
    )
}

// Маппинг NetworkLifeFlow -> LifeFlowEntity
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

// Маппинг NetworkTask -> TaskEntity
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
        payload = this.payload.toTaskPayload(),
        lifeAreaId = this.payload.lifeAreaId,
        flowId = null, // Будет установлено отдельно
        lifeAreaPlacement = this.lifeAreaPlacement,
        flowPlacement = this.flowPlacement,
        commentCount = this.commentCount,
        attachmentCount = this.attachmentCount
    )
}

// Маппинг NetworkTaskPayload -> TaskPayload
fun NetworkTaskPayload.toTaskPayload(): TaskPayload {
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

// Маппинг NetworkTaskAssignee -> TaskAssigneeEntity
fun NetworkTaskAssignee.toEntity(taskCardId: UUID): TaskAssigneeEntity {
    return TaskAssigneeEntity(
        taskCardId = taskCardId,
        employeeId = this.employeeId,
        complete = this.complete
    )
}

// Маппинг NetworkChecklistTask -> ChecklistTaskEntity
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