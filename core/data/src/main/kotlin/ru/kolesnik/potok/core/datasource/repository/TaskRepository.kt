package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.database.entitys.*
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.model.extensions.toEntity
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject

interface TaskRepository {
    suspend fun createTask(request: TaskRq): UUID
    suspend fun updateTask(taskId: UUID, request: PatchPayload)
    suspend fun moveTaskToFlow(taskId: UUID, request: FlowPositionRq)
    suspend fun moveTaskToLifeArea(taskId: UUID, request: LifeAreaPositionRq)
    suspend fun archiveTask(taskId: UUID)
    suspend fun restoreTask(taskId: UUID)
    suspend fun syncTaskDetails(taskId: UUID)
    suspend fun getTaskDetails(taskId: UUID): TaskEntity?
}

class TaskRepositoryImpl @Inject constructor(
    private val dataSource: SyncFullDataSource,
    private val taskDao: TaskDao,
    private val taskAssigneeDao: TaskAssigneeDao,
    private val checklistTaskDao: ChecklistTaskDao,
    private val taskCommentDao: TaskCommentDao
) : TaskRepository {

    override suspend fun createTask(request: TaskRq): UUID {
        // В демо-режиме создаем задачу локально
        val taskId = UUID.randomUUID()
        val taskEntity = TaskEntity(
            cardId = taskId,
            externalId = taskId.toString(),
            internalId = System.currentTimeMillis(),
            title = request.payload.title ?: "Новая задача",
            subtitle = request.payload.subtitle,
            mainOrder = null,
            source = request.payload.source,
            taskOwner = "449927", // Демо пользователь
            creationDate = OffsetDateTime.now(),
            payload = request.payload.toTaskPayload(),
            lifeAreaId = request.lifeAreaId,
            flowId = request.flowId,
            lifeAreaPlacement = null,
            flowPlacement = null,
            commentCount = 0,
            attachmentCount = 0
        )
        
        taskDao.insert(taskEntity)
        return taskId
    }

    override suspend fun updateTask(taskId: UUID, request: PatchPayload) {
        // В демо-режиме обновляем локально
        val existingTask = taskDao.getById(taskId)
        existingTask?.let { task ->
            val updatedPayload = task.payload.copy(
                title = request.title ?: task.payload.title,
                description = request.description ?: task.payload.description,
                important = request.important ?: task.payload.important,
                deadline = request.deadline ?: task.payload.deadline
            )
            
            val updatedTask = task.copy(
                title = request.title ?: task.title,
                payload = updatedPayload
            )
            
            taskDao.update(updatedTask)
        }
    }

    override suspend fun moveTaskToFlow(taskId: UUID, request: FlowPositionRq) {
        taskDao.moveToFlow(taskId, request.flowId, request.position)
    }

    override suspend fun moveTaskToLifeArea(taskId: UUID, request: LifeAreaPositionRq) {
        taskDao.moveToArea(taskId, request.lifeAreaId, request.position)
    }

    override suspend fun archiveTask(taskId: UUID) {
        taskDao.markAsArchived(taskId, OffsetDateTime.now())
    }

    override suspend fun restoreTask(taskId: UUID) {
        taskDao.restoreFromArchive(taskId)
    }

    override suspend fun syncTaskDetails(taskId: UUID) {
        // В демо-режиме ничего не делаем
    }

    override suspend fun getTaskDetails(taskId: UUID): TaskEntity? {
        return taskDao.getById(taskId)
    }
}

// Вспомогательная функция для конвертации
private fun TaskPayload.toTaskPayload(): ru.kolesnik.potok.core.model.TaskPayload {
    return ru.kolesnik.potok.core.model.TaskPayload(
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