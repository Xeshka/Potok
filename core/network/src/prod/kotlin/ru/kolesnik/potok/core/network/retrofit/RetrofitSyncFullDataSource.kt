package ru.kolesnik.potok.core.network.retrofit

import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.*
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.EmployeeId
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.api.LifeAreaDTO
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import javax.inject.Inject
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

interface RetrofitSyncFullApi {
    @GET("/api/service-task-main/api/v1/life-areas/full")
    suspend fun getFull(): List<NetworkLifeArea>

    @GET("/api/service-task-main/api/v1/life-areas/full")
    suspend fun gtFullNew(): List<LifeAreaDTO>

    @GET("/api/service-addressbook/api/v1/addressbook/employee/search")
    suspend fun getEmployee(
        @Query("employeeNumber") employeeNumbers: String,
        @Query("avatar") avatar: Boolean = true
    ): List<EmployeeResponse>

    @PATCH("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun patchTask(
        @Path("taskId") taskId: TaskExternalId,
        @Body task: PatchPayload
    )

    @POST("/api/service-task-main/api/v1/tasks")
    suspend fun createTask(@Body task: NetworkCreateTask): NetworkTask
}

@Singleton
class RetrofitSyncFullDataSource @Inject constructor(
    callFactory: Call.Factory,
    networkJson: Json
) : SyncFullDataSource {

    private val api = Retrofit.Builder()
        .baseUrl(RANDM_BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(
            Json {
                serializersModule = customSerializersModule
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(RetrofitSyncFullApi::class.java)

    override suspend fun getFull(): List<NetworkLifeArea> {
        return api.getFull()
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return api.gtFullNew()
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return employeeNumbers.flatMap { employeeNumber ->
            api.getEmployee(employeeNumber, avatar)
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        api.patchTask(taskId, task)
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return api.createTask(task)
    }
}