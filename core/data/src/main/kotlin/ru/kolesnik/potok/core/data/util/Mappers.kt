package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.*
import java.time.OffsetDateTime
import java.util.*

// Network DTO -> Entity mappers
fun LifeAreaDTO.toEntity(): LifeAreaEntity {
    return LifeAreaEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun LifeFlowDTO.toEntity(): LifeFlowEntity {
    return LifeFlowEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        lifeAreaId = this.lifeAreaId,
        status = this.status?.name ?: "ACTIVE",
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun TaskRs.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        priority = this.priority,
        isImportant = this.isImportant ?: false,
        deadline = this.deadline,
        lifeAreaId = this.lifeAreaId,
        lifeFlowId = this.lifeFlowId,
        assigneeIds = this.assigneeIds ?: emptyList(),
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
        completedAt = this.completedAt
    )
}

fun TaskCommentRs.toEntity(taskId: UUID): CommentEntity {
    return CommentEntity(
        id = this.id,
        taskId = taskId,
        authorId = this.authorId,
        content = this.content,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun ChecklistRs.toEntity(taskId: UUID): ChecklistTaskEntity {
    return ChecklistTaskEntity(
        id = this.id,
        taskId = taskId,
        title = this.title,
        isCompleted = this.isCompleted ?: false,
        position = this.position ?: 0,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

// Entity -> Model mappers
fun LifeAreaEntity.toModel(): LifeArea {
    return LifeArea(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun LifeFlowEntity.toModel(): LifeFlow {
    return LifeFlow(
        id = this.id,
        name = this.name,
        description = this.description,
        lifeAreaId = this.lifeAreaId,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun TaskEntity.toModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        priority = this.priority,
        isImportant = this.isImportant,
        deadline = this.deadline,
        lifeAreaId = this.lifeAreaId,
        lifeFlowId = this.lifeFlowId,
        assigneeIds = this.assigneeIds,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        completedAt = this.completedAt
    )
}

fun CommentEntity.toModel(): TaskComment {
    return TaskComment(
        id = this.id,
        taskId = this.taskId,
        authorId = this.authorId,
        content = this.content,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ChecklistTaskEntity.toModel(): ChecklistTask {
    return ChecklistTask(
        id = this.id,
        taskId = this.taskId,
        title = this.title,
        isCompleted = this.isCompleted,
        position = this.position,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

// Network DTO -> Model mappers (for search results without saving to DB)
fun LifeAreaDTO.toModel(): LifeArea {
    return LifeArea(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun LifeFlowDTO.toModel(): LifeFlow {
    return LifeFlow(
        id = this.id,
        name = this.name,
        description = this.description,
        lifeAreaId = this.lifeAreaId,
        status = this.status?.name ?: "ACTIVE",
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun TaskRs.toModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        priority = this.priority,
        isImportant = this.isImportant ?: false,
        deadline = this.deadline,
        lifeAreaId = this.lifeAreaId,
        lifeFlowId = this.lifeFlowId,
        assigneeIds = this.assigneeIds ?: emptyList(),
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now(),
        completedAt = this.completedAt
    )
}

fun TaskCommentRs.toModel(): TaskComment {
    return TaskComment(
        id = this.id,
        taskId = UUID.randomUUID(), // Временное значение, должно быть передано извне
        authorId = this.authorId,
        content = this.content,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

fun ChecklistRs.toModel(): ChecklistTask {
    return ChecklistTask(
        id = this.id,
        taskId = UUID.randomUUID(), // Временное значение, должно быть передано извне
        title = this.title,
        isCompleted = this.isCompleted ?: false,
        position = this.position ?: 0,
        createdAt = this.createdAt ?: OffsetDateTime.now(),
        updatedAt = this.updatedAt ?: OffsetDateTime.now()
    )
}

// Employee mappers
fun EmployeeResponse.toModel(): Employee {
    return Employee(
        id = this.id,
        name = this.name,
        email = this.email,
        avatar = this.avatar,
        position = this.position,
        department = this.department
    )
}

// Model -> Request mappers
fun Task.toCreateRequest(): TaskRs {
    return TaskRs(
        id = this.id,
        title = this.title,
        description = this.description,
        status = this.status,
        priority = this.priority,
        isImportant = this.isImportant,
        deadline = this.deadline,
        lifeAreaId = this.lifeAreaId,
        lifeFlowId = this.lifeFlowId,
        assigneeIds = this.assigneeIds,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        completedAt = this.completedAt
    )
}

fun TaskComment.toCreateRequest(): TaskCommentRq {
    return TaskCommentRq(
        id = this.id,
        taskId = this.taskId,
        authorId = this.authorId,
        content = this.content,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ChecklistTask.toCreateRequest(): ChecklistRq {
    return ChecklistRq(
        id = this.id,
        taskId = this.taskId,
        title = this.title,
        isCompleted = this.isCompleted,
        position = this.position,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun LifeArea.toCreateRequest(): LifeAreaDTO {
    return LifeAreaDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun LifeFlow.toCreateRequest(): LifeFlowDTO {
    return LifeFlowDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        lifeAreaId = this.lifeAreaId,
        status = try { 
            FlowStatus.valueOf(this.status) 
        } catch (e: IllegalArgumentException) { 
            FlowStatus.ACTIVE 
        },
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}