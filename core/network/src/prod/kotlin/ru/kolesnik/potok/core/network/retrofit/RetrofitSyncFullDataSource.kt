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
import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.AddressbookSource
import javax.inject.Inject
import javax.inject.Singleton

private const val BACKEND_URL = BuildConfig.BACKEND_URL

@Singleton
internal class RetrofitSyncFullDataSource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : SyncFullDataSource, AddressbookSource {

    companion object {
        private const val TAG = "RetrofitSyncFullDataSource"
    }

    private val networkApi = trace("RetrofitSyncFullDataSource") {
        try {
            Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .callFactory { okhttpCallFactory.get().newCall(it) }
                .addConverterFactory(
                    networkJson.asConverterFactory("application/json".toMediaType())
                )
                .build()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create Retrofit instance", e)
            throw e
        }
    }

    private val lifeAreaApi by lazy { 
        try {
            networkApi.create(LifeAreaApi::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create LifeAreaApi", e)
            throw e
        }
    }
    
    private val taskApi by lazy { 
        try {
            networkApi.create(TaskApi::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create TaskApi", e)
            throw e
        }
    }

    override suspend fun getFull(): List<NetworkLifeArea> {
        return try {
            Log.d(TAG, "Fetching full data...")
            // Здесь должна быть реальная реализация
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch full data", e)
            throw e
        }
    }

    override suspend fun gtFullNew(): List<LifeAreaDTO> {
        return try {
            Log.d(TAG, "Fetching full new data...")
            lifeAreaApi.getFullLifeAreas()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch full new data", e)
            throw e
        }
    }

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return try {
            Log.d(TAG, "Fetching employees: $employeeNumbers")
            // Здесь должна быть реальная реализация
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch employees", e)
            throw e
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        try {
            Log.d(TAG, "Patching task: $taskId")
            taskApi.updateTask(taskId, task)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to patch task: $taskId", e)
            throw e
        }
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return try {
            Log.d(TAG, "Creating task...")
            // Здесь должна быть реальная реализация
            throw NotImplementedError("Create task not implemented in prod")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create task", e)
            throw e
        }
    }
}