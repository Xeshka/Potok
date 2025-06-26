package ru.kolesnik.potok.core.network

import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload

interface SyncFullDataSource {
    suspend fun getFull(): List<NetworkLifeArea>
    suspend fun gtFullNew(): List<LifeAreaDTO>
    suspend fun getEmployee(employeeNumbers: List<EmployeeId>, avatar: Boolean): List<EmployeeResponse>
    suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload)
    suspend fun createTask(task: NetworkCreateTask): NetworkTask
}