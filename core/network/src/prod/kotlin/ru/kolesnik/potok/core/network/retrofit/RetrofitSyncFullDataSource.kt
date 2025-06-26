package ru.kolesnik.potok.core.network.retrofit

import android.util.Log
import androidx.tracing.trace
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.retrofit.LifeAreaApi
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "RetrofitSyncFullDataSource"
private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

@Singleton
internal class RetrofitSyncFullDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : SyncFullDataSource {

    private val lifeAreaApi = trace("RetrofitSyncFullDataSource") {
        Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                GlobalJson.instance.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(LifeAreaApi::class.java)
    }

    override suspend fun getFull(): List<NetworkLifeArea> {
        return try {
            Log.d(TAG, "Fetching full data from: $RANDM_BASE_URL")
            val result = lifeAreaApi.getFullLifeAreas()
            Log.d(TAG, "Successfully fetched ${result.size} life areas")
            
            // Конвертируем LifeAreaDTO в NetworkLifeArea
            result.map { dto ->
                NetworkLifeArea(
                    id = dto.id,
                    title = dto.title,
                    flows = dto.flows?.map { flowDto ->
                        ru.kolesnik.potok.core.network.model.potok.NetworkLifeFlow(
                            id = flowDto.id,
                            areaId = flowDto.areaId,
                            title = flowDto.title,
                            style = flowDto.style,
                            placement = flowDto.placement,
                            status = flowDto.status?.name,
                            tasks = flowDto.tasks?.map { taskDto ->
                                NetworkTask(
                                    id = taskDto.id,
                                    title = taskDto.title,
                                    subtitle = taskDto.subtitle,
                                    mainOrder = taskDto.mainOrder,
                                    source = taskDto.source,
                                    taskOwner = taskDto.taskOwner,
                                    creationDate = taskDto.creationDate,
                                    payload = ru.kolesnik.potok.core.network.model.potok.NetworkTaskPayload(
                                        title = taskDto.payload.title,
                                        source = taskDto.payload.source,
                                        onMainPage = taskDto.payload.onMainPage,
                                        deadline = taskDto.payload.deadline,
                                        lifeArea = taskDto.payload.lifeArea,
                                        lifeAreaId = taskDto.payload.lifeAreaId,
                                        subtitle = taskDto.payload.subtitle,
                                        userEdit = taskDto.payload.userEdit,
                                        assignees = taskDto.payload.assignees,
                                        important = taskDto.payload.important,
                                        messageId = taskDto.payload.messageId,
                                        fullMessage = taskDto.payload.fullMessage,
                                        description = taskDto.payload.description,
                                        priority = taskDto.payload.priority,
                                        userChangeAssignee = taskDto.payload.userChangeAssignee,
                                        organization = taskDto.payload.organization,
                                        shortMessage = taskDto.payload.shortMessage,
                                        externalId = taskDto.payload.externalId,
                                        relatedAssignment = taskDto.payload.relatedAssignment,
                                        meanSource = taskDto.payload.meanSource,
                                        id = taskDto.payload.id
                                    ),
                                    internalId = taskDto.internalId,
                                    lifeAreaPlacement = taskDto.lifeAreaPlacement,
                                    flowPlacement = taskDto.flowPlacement,
                                    assignees = taskDto.assignees?.map { assigneeDto ->
                                        ru.kolesnik.potok.core.network.model.potok.NetworkTaskAssignee(
                                            employeeId = assigneeDto.employeeId,
                                            complete = assigneeDto.complete
                                        )
                                    },
                                    commentCount = taskDto.commentCount,
                                    attachmentCount = taskDto.attachmentCount,
                                    checkList = taskDto.checkList?.map { checkDto ->
                                        ru.kolesnik.potok.core.network.model.potok.NetworkChecklistTask(
                                            id = checkDto.id,
                                            title = checkDto.title,
                                            done = checkDto.done,
                                            placement = checkDto.placement,
                                            responsibles = checkDto.responsibles,
                                            deadline = checkDto.deadline
                                        )
                                    },
                                    cardId = taskDto.cardId
                                )
                            }
                        )
                    },
                    style = dto.style,
                    tagsId = dto.tagsId,
                    placement = dto.placement,
                    isDefault = dto.isDefault,
                    sharedInfo = dto.sharedInfo?.let { sharedDto ->
                        ru.kolesnik.potok.core.network.model.potok.NetworkLifeAreaSharedInfo(
                            owner = sharedDto.owner,
                            readOnly = sharedDto.readOnly,
                            expiredDate = null, // LocalDate не поддерживается в NetworkLifeAreaSharedInfo
                            recipients = sharedDto.recipients
                        )
                    },
                    isTheme = dto.isTheme,
                    onlyPersonal = dto.onlyPersonal
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch full data", e)
            throw e
        }
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return try {
            Log.d(TAG, "Fetching new full data from: $RANDM_BASE_URL")
            val result = lifeAreaApi.getFullLifeAreas()
            Log.d(TAG, "Successfully fetched ${result.size} life areas (new format)")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch new full data", e)
            throw e
        }
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return try {
            Log.d(TAG, "Fetching employees: $employeeNumbers")
            // В продакшн-версии нужно реализовать получение сотрудников
            // Пока возвращаем заглушки
            employeeNumbers.map { employeeId ->
                EmployeeResponse(
                    employeeNumber = employeeId,
                    timezone = "3",
                    terBank = "ЦА",
                    employeeId = employeeId,
                    lastName = "Сотрудник",
                    firstName = "Тест",
                    middleName = "Тестович",
                    position = "Разработчик",
                    mainEmail = "$employeeId@company.ru",
                    avatar = null
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch employees", e)
            emptyList()
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        try {
            Log.d(TAG, "Patching task $taskId")
            // Здесь должна быть реализация обновления задачи через API
            Log.d(TAG, "Task $taskId patched successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to patch task $taskId", e)
            throw e
        }
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        try {
            Log.d(TAG, "Creating new task")
            // Здесь должна быть реализация создания задачи через API
            // Пока возвращаем заглушку
            val taskId = java.util.UUID.randomUUID().toString().substring(0, 8)
            val cardId = java.util.UUID.randomUUID()
            
            val result = NetworkTask(
                id = taskId,
                title = task.payload.title ?: "Новая задача",
                taskOwner = "current_user", // Здесь должен быть ID текущего пользователя
                creationDate = java.time.OffsetDateTime.now(),
                payload = task.payload,
                cardId = cardId,
                internalId = System.currentTimeMillis(),
                lifeAreaPlacement = 0,
                flowPlacement = 0,
                assignees = task.payload.assignees?.map { 
                    ru.kolesnik.potok.core.network.model.potok.NetworkTaskAssignee(
                        employeeId = it,
                        complete = false
                    )
                }
            )
            
            Log.d(TAG, "Task created successfully with ID: $taskId")
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create task", e)
            throw e
        }
    }
}

object GlobalJson {
    val instance = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
        prettyPrint = true
        isLenient = true
        encodeDefaults = true
    }
}