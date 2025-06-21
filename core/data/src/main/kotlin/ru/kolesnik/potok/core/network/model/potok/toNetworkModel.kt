package ru.kolesnik.potok.core.network.model.potok

import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskPayload

fun Task.toNetworkModel(): NetworkTask {
    return NetworkTask(
        id = id!!,
        title = title,
        subtitle = subtitle,
        mainOrder = mainOrder,
        source = source,
        taskOwner = taskOwner,
        creationDate = creationDate?.toString()
            ?: throw IllegalArgumentException("Creation date is required"),
        payload = payload?.toNetworkModel(),
        internalId = internalId,
        lifeAreaPlacement = lifeAreaPlacement,
        flowPlacement = flowPlacement,
        assignees = assignees?.map { it.toNetworkModel() },
        metrics = emptyList(),
        commentCount = commentCount,
        attachmentCount = attachmentCount,
    )
}

fun Task.toNetworkCreateModel(): NetworkCreateTask {
    return NetworkCreateTask(
        flowId = flowId.toString(),
        lifeAreaId = lifeAreaId.toString(),
        payload = payload!!.toNetworkCreateModel(),
    )
}

fun TaskPayload.toNetworkCreateModel(): NetworkCreateTask.NetworkCreateTaskPayload {
    return NetworkCreateTask.NetworkCreateTaskPayload(
        title = title,
        deadline = deadline?.toString(),
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId?.toString(),
        assignees = assignees,
        important = important,
        description = description,
        externalId = externalId,
    )
}

fun TaskPayload.toNetworkModel(): NetworkTaskPayload {
    return NetworkTaskPayload(
        title = title,
        source = source,
        onMainPage = onMainPage,
        deadline = deadline?.toString(),
        lifeArea = lifeArea,
        lifeAreaId = lifeAreaId?.toString(),
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
        meanSource = meanSource,
        id = id
    )
}

fun TaskAssignee.toNetworkModel(): NetworkTaskAssignee {
    return NetworkTaskAssignee(
        employeeId = employeeId,
        complete = complete
    )
}

fun NetworkTaskPayload.toPatchPayload(): PatchPayload {
    return PatchPayload(
        title = title,
        source = source,
        deadline = deadline,
        assignees = assignees,
        important = important ?: false,
        description = description,
        priority = priority,
        externalId = externalId
    )
}