package ru.kolesnik.potok.core.network.repository

import ru.kolesnik.potok.core.model.Task
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.NetworkTaskPayload
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import javax.inject.Inject

interface FullProjectRepository {
    suspend fun sync()
    suspend fun getEmployee(employeeNumbers: List<EmployeeId>): List<EmployeeResponse>
    suspend fun updateTask(taskId: String, task: PatchPayload)
    suspend fun createTask(task: Task): NetworkTask
}

class FullProjectRepositoryImpl @Inject constructor(
    private val syncRepository: SyncRepository,
    private val syncDataSource: SyncFullDataSource
) : FullProjectRepository {

    override suspend fun sync() {
        // Используем SyncRepository для синхронизации данных
        syncRepository.sync()
    }

    override suspend fun getEmployee(employeeNumbers: List<EmployeeId>): List<EmployeeResponse> {
        return syncDataSource.getEmployee(employeeNumbers, true)
    }

    override suspend fun updateTask(taskId: String, task: PatchPayload) {
        syncDataSource.patchTask(taskId, task)
    }

    override suspend fun createTask(task: Task): NetworkTask {
        // Преобразуем модель Task в NetworkCreateTask
        val createTask = NetworkCreateTask(
            lifeAreaId = task.lifeAreaId,
            flowId = task.flowId,
            payload = NetworkTaskPayload(
                title = task.title,
                source = task.source,
                onMainPage = task.payload?.onMainPage,
                deadline = task.payload?.deadline,
                lifeArea = task.payload?.lifeArea,
                lifeAreaId = task.payload?.lifeAreaId,
                subtitle = task.subtitle,
                userEdit = task.payload?.userEdit,
                assignees = task.payload?.assignees,
                important = task.payload?.important,
                description = task.payload?.description,
                priority = task.payload?.priority,
                externalId = task.payload?.externalId,
                meanSource = task.payload?.meanSource
            )
        )
        
        return syncDataSource.createTask(createTask)
    }
}