package ru.kolesnik.potok.core.network.demo

import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.kolesnik.potok.core.network.JvmUnitTestDemoAssetManager
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import java.io.BufferedReader
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoSyncFullDataSource @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DemoAssetManager = JvmUnitTestDemoAssetManager,
) : SyncFullDataSource {

    /**
     * Get data from the given JSON [fileName].
     */
    @OptIn(ExperimentalSerializationApi::class)
    private suspend inline fun <reified T> getDataFromJsonFile(fileName: String): T =
        withContext(ioDispatcher) {
            try {
                assets.open(fileName).use { inputStream ->
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        inputStream.bufferedReader().use(BufferedReader::readText)
                            .let { networkJson.decodeFromString(it) }
                    } else {
                        networkJson.decodeFromStream(inputStream)
                    }
                }
            } catch (e: Exception) {
                // Возвращаем пустой список в случае ошибки
                when (T::class) {
                    List::class -> emptyList<Any>() as T
                    else -> throw e
                }
            }
        }

    companion object {
        private const val FULL_DATA_FILE = "full copy copy copy.json"
        private const val EMPLOYEE_FILE = "employee.json"
    }

    override suspend fun getFull(): List<NetworkLifeArea> {
        return try {
            getDataFromJsonFile(FULL_DATA_FILE)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return try {
            getDataFromJsonFile(FULL_DATA_FILE)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return try {
            // Возвращаем заглушечные данные о сотрудниках
            listOf(
                EmployeeResponse(
                    employeeNumber = "449927",
                    timezone = "3",
                    terBank = "ЦА",
                    employeeId = "449927",
                    lastName = "Колесник",
                    firstName = "Никита",
                    middleName = "Сергеевич",
                    position = "Главный инженер по разработке",
                    mainEmail = "NSKolesnik@sberbank.ru",
                    avatar = "/api/service-addressbook/api/v1/addressbook/employee/449927/userphoto.jpeg"
                ),
                EmployeeResponse(
                    employeeNumber = "91112408208",
                    timezone = "3",
                    terBank = "ЦА",
                    employeeId = "91112408208",
                    lastName = "Иванов",
                    firstName = "Иван",
                    middleName = "Иванович",
                    position = "Разработчик",
                    mainEmail = "IIIvanov@sberbank.ru",
                    avatar = null
                ),
                EmployeeResponse(
                    employeeNumber = "1796367",
                    timezone = "3",
                    terBank = "ЦА",
                    employeeId = "1796367",
                    lastName = "Петров",
                    firstName = "Петр",
                    middleName = "Петрович",
                    position = "Менеджер",
                    mainEmail = "PPPetrov@sberbank.ru",
                    avatar = null
                )
            ).filter { employeeNumbers.contains(it.employeeId) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        // В демо-режиме просто логируем действие
        println("Demo mode: Patching task $taskId with $task")
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return try {
            // Создаем заглушечную задачу с уникальным ID
            val taskId = UUID.randomUUID().toString().substring(0, 8)
            val cardId = UUID.randomUUID()
            
            NetworkTask(
                id = taskId,
                title = task.payload.title ?: "Новая задача",
                taskOwner = "449927", // Текущий пользователь
                creationDate = java.time.OffsetDateTime.now(),
                payload = task.payload,
                cardId = cardId,
                internalId = System.currentTimeMillis(),
                lifeAreaId = task.lifeAreaId,
                flowId = task.flowId,
                assignees = task.payload.assignees?.map { 
                    ru.kolesnik.potok.core.network.model.potok.NetworkTaskAssignee(
                        employeeId = it,
                        complete = false
                    )
                }
            )
        } catch (e: Exception) {
            // Возвращаем базовую заглушку
            NetworkTask(
                id = "demo_task",
                title = "Demo Task",
                taskOwner = "449927",
                creationDate = java.time.OffsetDateTime.now(),
                payload = task.payload,
                cardId = UUID.randomUUID()
            )
        }
    }

    fun getFullLifeAreas(): List<LifeAreaDTO> {
        return try {
            runCatching { 
                kotlinx.coroutines.runBlocking { 
                    gtFullNew() 
                } 
            }.getOrDefault(emptyList())
        } catch (e: Exception) {
            emptyList()
        }
    }
}