package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import java.time.OffsetDateTime
import java.util.*

// Network DTO to Entity mappers
fun LifeAreaDTO.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )
}

fun TaskRs.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        lifeFlowId = this.lifeFlowId,
        status = this.status,
        priority = this.priority,
        deadline = this.deadline?.let { OffsetDateTime.parse(it) },
        isImportant = this.isImportant ?: false,
        createdAt = this.createdAt?.let { OffsetDateTime.parse(it) } ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt?.let { OffsetDateTime.parse(it) } ?: OffsetDateTime.now(),
        assigneeIds = this.assigneeIds ?: emptyList()
    )
}

fun ChecklistRq.toEntity(): ChecklistTaskEntity {
    return ChecklistTaskEntity(
        id = UUID.randomUUID().toString(),
        taskId = this.taskId,
        title = this.title,
        isCompleted = false,
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )
}

fun TaskCommentRq.toEntity(): CommentEntity {
    return CommentEntity(
        id = UUID.randomUUID().toString(),
        taskId = this.taskId,
        text = this.text,
        authorId = this.authorId ?: "",
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )
}

fun EmployeeResponse.toEntity(): Employee {
    return Employee(
        id = this.id,
        name = this.name,
        email = this.email,
        avatar = this.avatar
    )
}

// Entity to Model mappers
fun LifeAreaEntity.toModel(): LifeArea {
    return LifeArea(
        id = this.id,
        name = this.name,
        description = this.description
    )
}

fun LifeFlowEntity.toModel(): LifeFlow {
    return LifeFlow(
        id = this.id,
        name = this.name,
        description = this.description,
        lifeAreaId = this.lifeAreaId,
        status = this.status
    )
}

fun TaskEntity.toModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        lifeFlowId = this.lifeFlowId,
        status = this.status,
        priority = this.priority,
        deadline = this.deadline?.toString(),
        isImportant = this.isImportant,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        assigneeIds = this.assigneeIds
    )
}

fun ChecklistTaskEntity.toModel(): ChecklistTask {
    return ChecklistTask(
        id = this.id,
        taskId = this.taskId,
        title = this.title,
        isCompleted = this.isCompleted,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}

fun CommentEntity.toModel(): TaskComment {
    return TaskComment(
        id = this.id,
        taskId = this.taskId,
        text = this.text,
        authorId = this.authorId,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}

// Network DTO to Model mappers (for search results that don't need to be stored)
fun TaskRs.toModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        lifeFlowId = this.lifeFlowId,
        status = this.status,
        priority = this.priority,
        deadline = this.deadline,
        isImportant = this.isImportant ?: false,
        createdAt = this.createdAt ?: "",
        updatedAt = this.updatedAt ?: "",
        assigneeIds = this.assigneeIds ?: emptyList()
    )
}

fun EmployeeResponse.toModel(): Employee {
    return Employee(
        id = this.id,
        name = this.name,
        email = this.email,
        avatar = this.avatar
    )
}