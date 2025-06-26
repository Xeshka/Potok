package ru.kolesnik.potok.core.network.retrofit

import androidx.tracing.trace
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.model.customSerializersModule
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL
private val employeeCache = ConcurrentHashMap<String, EmployeeResponse>()

@Singleton
internal class RetrofitSyncFullDataSource @Inject constructor(
    private val lifeAreaApi: LifeAreaApi,
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : SyncFullDataSource {

    override suspend fun getFull(): List<NetworkLifeArea> {
        // Конвертируем LifeAreaDTO в NetworkLifeArea
        val lifeAreas = lifeAreaApi.getFullLifeAreas()
        return lifeAreas.map { dto ->
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
                            ru.kolesnik.potok.core.network.model.potok.NetworkTask(
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
                                    descriptionDelta = taskDto.payload.descriptionDelta?.map { op ->
                                        ru.kolesnik.potok.core.network.model.potok.NetworkOp(
                                            insert = op.insert
                                        )
                                    },
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
                        expiredDate = null, // LocalDate -> OffsetDateTime conversion needed
                        recipients = sharedDto.recipients
                    )
                },
                isTheme = dto.isTheme,
                onlyPersonal = dto.onlyPersonal
            )
        }
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return lifeAreaApi.getFullLifeAreas()
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        // Заглушка для получения сотрудников
        return employeeNumbers.distinct().map { employeeNumber ->
            employeeCache.getOrPut(employeeNumber) {
                EmployeeResponse(
                    employeeNumber = employeeNumber,
                    timezone = "3",
                    terBank = "ЦА",
                    employeeId = employeeNumber,
                    lastName = "Фамилия",
                    firstName = "Имя",
                    middleName = "Отчество",
                    position = "Должность",
                    mainEmail = "$employeeNumber@company.ru",
                    avatar = if (avatar) "/avatar/$employeeNumber.jpg" else null
                )
            }
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        // Заглушка для обновления задачи
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        // Заглушка для создания задачи
        val taskId = java.util.UUID.randomUUID().toString().substring(0, 8)
        val cardId = java.util.UUID.randomUUID()
        
        return NetworkTask(
            id = taskId,
            title = task.payload.title ?: "Новая задача",
            taskOwner = "449927",
            creationDate = java.time.OffsetDateTime.now(),
            payload = task.payload,
            cardId = cardId,
            internalId = System.currentTimeMillis(),
            assignees = task.payload.assignees?.map { 
                ru.kolesnik.potok.core.network.model.potok.NetworkTaskAssignee(
                    employeeId = it,
                    complete = false
                )
            }
        )
    }
}