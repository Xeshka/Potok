package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.kolesnik.potok.core.database.dao.TaskDao
import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.model.TaskAssignee
import ru.kolesnik.potok.core.model.TaskMain
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.potok.toAssigneeEntities
import ru.kolesnik.potok.core.network.model.potok.toDomainModel
import ru.kolesnik.potok.core.network.model.potok.toEntity
import ru.kolesnik.potok.core.network.model.potok.toNetworkCreateModel
import ru.kolesnik.potok.core.network.model.potok.toNetworkModel
import ru.kolesnik.potok.core.network.model.potok.toPatchPayload
import ru.kolesnik.potok.core.network.model.potok.toPayloadEntity
import ru.kolesnik.potok.core.network.model.potok.toTaskDomain
import ru.kolesnik.potok.core.network.model.potok.toTaskEntities
import javax.inject.Inject

class DefaultTaskRepository @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val taskDao: TaskDao,
) : TaskRepository {

    override fun getTaskMainByArea(areaId: String): Flow<List<TaskMain>> {
        return taskDao.getTasksForArea(areaId)
            .flatMapLatest { tasks ->
                combine(
                    tasks.map { task ->
                        taskDao.getAssigneesForTask(task.id)
                            .map { assignees -> task to assignees }
                    }
                ) { assignments ->
                    assignments.map { (task, assignees) ->
                        task.toDomainModel(
                            assignees.toList(),
                            taskDao.getDeadline(task.id).first().firstOrNull()
                        )
                    }
                }
            }
    }

    override suspend fun getTaskById(taskId: String): Task {


        return withContext(Dispatchers.IO) {
            val entity = taskDao.getTaskById(taskId) ?: throw Exception("Task not found")
            val assignees = taskDao.getAssigneesForTask(taskId)
                .first()
                .map { TaskAssignee(it.employeeId, it.complete) }
            val payload = taskDao.getPayloadForTask(taskId)?.toDomainModel(assignees.map { it.employeeId })
            entity.toTaskDomain(payload, assignees)
        }
    }

    override suspend fun createTask(task: Task) {
        val createdTask = syncFullDataSource.createTask(task.toNetworkCreateModel())
            .toTaskEntities(task.flowId.toString(), task.lifeAreaId.toString())
        taskDao.insertOrUpdateTask(createdTask.task)
        createdTask.payload?.let { taskDao.insertOrUpdateTaskPayload(it) }
        createdTask.assignees?.onEach {
            taskDao.addTaskAssignee(it)
        }
    }

    override suspend fun updateAndGetTask(task: Task): Task {
        if (task.id != null) {
            syncFullDataSource.patchTask(task.id!!, task.toNetworkModel().payload!!.toPatchPayload())
            taskDao.insertOrUpdateTask(task.toEntity())
            task.toPayloadEntity()?.let { taskDao.insertOrUpdateTaskPayload(it) }
            task.toAssigneeEntities().onEach {
                taskDao.addTaskAssignee(it)
            }
            return getTaskById(task.id!!)
        } else throw Exception("TASK_NOT_EXIST")
    }

    override suspend fun deleteTask(taskId: String) {
        taskDao.deleteTask(taskId)
        taskDao.deletePayloadTask(taskId)
        taskDao.deleteAssigneesTask(taskId)
    }

    override suspend fun closeTask(taskId: String, flowId: String) {
        taskDao.closeTask(taskId, flowId)
    }

}