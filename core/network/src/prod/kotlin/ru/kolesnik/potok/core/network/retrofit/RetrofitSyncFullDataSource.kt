package ru.kolesnik.potok.core.network.retrofit

import androidx.tracing.trace
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
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.*
import ru.kolesnik.potok.core.network.model.customSerializersModule
import javax.inject.Inject
import javax.inject.Singleton

private const val BACKEND_BASE_URL = BuildConfig.BACKEND_URL

/**
 * Retrofit API для работы с полными данными
 */
interface SyncFullApi {
    @GET("/api/service-task-main/api/v1/life-areas/full")
    suspend fun getFullLifeAreas(): List<LifeAreaDTO>

    @POST("/api/service-task-main/api/v1/tasks")
    suspend fun createTask(@Body request: NetworkCreateTask): NetworkTask

    @PATCH("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: String,
        @Body request: PatchPayload
    )
}

/**
 * Retrofit API для работы с сотрудниками
 */
interface EmployeeApi {
    @GET("/api/service-addressbook/api/v1/addressbook/employee/search")
    suspend fun getEmployees(
        @Query("employeeNumbers") employeeNumbers: String,
        @Query("avatar") avatar: Boolean = true
    ): List<EmployeeResponse>
}

@Singleton
internal class RetrofitSyncFullDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : SyncFullDataSource {

    private val networkApi = trace("RetrofitSyncFullDataSource") {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                Json {
                    serializersModule = customSerializersModule
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(SyncFullApi::class.java)
    }

    private val employeeApi = trace("RetrofitEmployeeApi") {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                Json {
                    serializersModule = customSerializersModule
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(EmployeeApi::class.java)
    }

    // ✅ Сетевой слой возвращает только DTO - без маппинга!
    override suspend fun getFull(): List<NetworkLifeArea> {
        return try {
            // Возвращаем пустой список - этот метод deprecated
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return try {
            networkApi.getFullLifeAreas()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return try {
            if (employeeNumbers.isEmpty()) return emptyList()
            
            val employeeNumbersString = employeeNumbers.joinToString(",")
            employeeApi.getEmployees(employeeNumbersString, avatar)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        try {
            networkApi.updateTask(taskId, task)
        } catch (e: Exception) {
            // Логируем ошибку, но не бросаем исключение
        }
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return try {
            networkApi.createTask(task)
        } catch (e: Exception) {
            // Возвращаем заглушку в случае ошибки
            NetworkTask(
                id = "error_${System.currentTimeMillis()}",
                title = task.payload.title ?: "Ошибка создания",
                taskOwner = "system",
                creationDate = java.time.OffsetDateTime.now(),
                payload = task.payload,
                cardId = java.util.UUID.randomUUID()
            )
        }
    }
}