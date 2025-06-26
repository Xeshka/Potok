package ru.kolesnik.potok.core.model.extensions

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import java.util.UUID

// Маппер из EmployeeResponse в Employee
fun EmployeeResponse.toDomain(): Employee {
    return Employee(
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
}

// Маппер из LifeAreaDTO в LifeArea
fun LifeAreaDTO.toDomain(): LifeArea {
    return LifeArea(
        id = id,
        title = title,
        style = style,
        tagsId = tagsId,
        placement = placement,
        isDefault = isDefault,
        isTheme = isTheme,
        shared = sharedInfo?.toDomain()
    )
}

// Маппер из LifeAreaSharedInfo в модель домена
fun LifeAreaSharedInfo.toDomain(): ru.kolesnik.potok.core.model.LifeAreaSharedInfo {
    return ru.kolesnik.potok.core.model.LifeAreaSharedInfo(
        areaId = UUID.randomUUID(), // Здесь нужно передавать правильный ID сферы
        owner = owner,
        recipients = recipients
    )
}

// Маппер из LifeFlowDTO в LifeFlow
fun LifeFlowDTO.toDomain(): LifeFlow {
    return LifeFlow(
        id = id.toString(),
        areaId = areaId.toString(),
        title = title,
        style = style,
        placement = placement,
        status = status?.toDomain() ?: FlowStatus.NEW
    )
}

// Маппер из FlowStatus API в FlowStatus модели
fun FlowStatus.toDomain(): ru.kolesnik.potok.core.model.FlowStatus {
    return when (this) {
        FlowStatus.NEW -> ru.kolesnik.potok.core.model.FlowStatus.NEW
        FlowStatus.IN_PROGRESS -> ru.kolesnik.potok.core.model.FlowStatus.IN_PROGRESS
        FlowStatus.COMPLETED -> ru.kolesnik.potok.core.model.FlowStatus.COMPLETED
        FlowStatus.CUSTOM -> ru.kolesnik.potok.core.model.FlowStatus.CUSTOM
    }
}

// Маппер из TaskRs в Task
fun TaskRs.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate,
        payload = payload.toDomain(),
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = assignees?.map { it.toDomain() },
        commentCount = commentCount,
        attachmentCount = attachmentCount,
        checkList = checkList?.map { it.toDomain() },
        lifeAreaId = payload.lifeAreaId,
        flowId = null // Нужно заполнить из контекста
    )
}

// Маппер из TaskPayload API в TaskPayload модели
fun TaskPayload.toDomain(): ru.kolesnik.potok.core.model.TaskPayload {
    return ru.kolesnik.potok.core.model.TaskPayload(
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
        meanSource = meanSource
    )
}

// Маппер из TaskAssigneeRs в TaskAssignee
fun TaskAssigneeRs.toDomain(): TaskAssignee {
    return TaskAssignee(
        employeeId = employeeId,
        complete = complete
    )
}

// Маппер из ChecklistTaskDTO в ChecklistTask
fun ChecklistTaskDTO.toDomain(): ChecklistTask {
    return ChecklistTask(
        id = id,
        title = title,
        done = done ?: false,
        placement = placement ?: 0,
        responsibles = responsibles,
        deadline = deadline
    )
}