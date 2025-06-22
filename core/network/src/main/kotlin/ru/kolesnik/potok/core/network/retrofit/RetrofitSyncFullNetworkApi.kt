package ru.kolesnik.potok.core.network.retrofit

import androidx.tracing.trace
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.di.ApiModule
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

interface RetrofitSyncFullNetworkApi {
    @GET("/api/service-task-main/api/v1/life-areas/full")
    suspend fun getFull(): List<NetworkLifeArea>

    @GET("/api/service-addressbook/api/v1/addressbook/employee/search")
    suspend fun getEmployee(
        @Query("employeeNumber") employeeNumber: String,
        @Query("avatar") avatar: Boolean,
    ): List<EmployeeResponse>

    @GET("/")
    @Headers("Accept: text/html")
    suspend fun auth()

    @PATCH("/api/service-task-main/api/v1/tasks/{taskId}")
    suspend fun patchTask(
        @Path("taskId") taskId: String,
        @Body task: PatchPayload
    )

    @POST("/api/service-task-main/api/v1/tasks")
    suspend fun createTask(
        @Body task: NetworkCreateTask
    ): NetworkTask
}

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL
private val employeeCache = ConcurrentHashMap<String, EmployeeResponse>()

@Singleton
internal class RetrofitSyncFull @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
    private val retrofit: Retrofit
) : SyncFullDataSource {

    private val networkApi = trace("RetrofitRandmNetwork") {
        Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                GlobalJson.instance.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(RetrofitSyncFullNetworkApi::class.java)
    }

    override suspend fun getFull(): List<NetworkLifeArea> {
        return networkApi.getFull()
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return ApiModule.provideLifeAreaApi(retrofit).getFullLifeAreas()
    }

    override suspend fun getEmployee(
        employeeNumbers: List<String>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return employeeNumbers.distinct().mapNotNull { employeeNumber ->
            employeeCache[employeeNumber] ?: run {
                networkApi.getEmployee(employeeNumber, avatar = true).firstOrNull()
                    .also {
                        if (it != null) employeeCache[employeeNumber] = it
                    }
            }
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        networkApi.patchTask(taskId, task)
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return networkApi.createTask(task)
    }

}

object GlobalJson {
    val instance = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true      // Игнорировать неизвестные ключи
        explicitNulls = false         // Не требовать null для отсутствующих полей
        coerceInputValues = true      // Автоматически преобразовывать неверные значения
        prettyPrint = true            // Для красивого вывода при сериализации
        isLenient = true              // Ленивый парсинг
        encodeDefaults = true         // Сериализовать значения по умолчанию
    }
}