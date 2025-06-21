package ru.kolesnik.potok.core.network.retrofit

import androidx.tracing.trace
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.TaskExternalId
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import javax.inject.Inject
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

@Singleton
internal class ExtendedRetrofitSyncFull @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : SyncFullDataSource {

    private val retrofit = trace("ExtendedRetrofitNetwork") {
        Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                GlobalJson.instance.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    // API instances
    val checklistApi: ChecklistApi = retrofit.create(ChecklistApi::class.java)
    val commentApi: CommentApi = retrofit.create(CommentApi::class.java)
    val lifeAreaApi: LifeAreaApi = retrofit.create(LifeAreaApi::class.java)
    val lifeFlowApi: LifeFlowApi = retrofit.create(LifeFlowApi::class.java)
    val taskArchiveApi: TaskArchiveApi = retrofit.create(TaskArchiveApi::class.java)
    val searchApi: SearchApi = retrofit.create(SearchApi::class.java)
    val taskManagementApi: TaskManagementApi = retrofit.create(TaskManagementApi::class.java)
    
    // Existing methods from RetrofitSyncFullNetworkApi
    private val networkApi = retrofit.create(RetrofitSyncFullNetworkApi::class.java)

    override suspend fun getFull(): List<NetworkLifeArea> {
        return networkApi.getFull()
    }

    override suspend fun getEmployee(
        employeeNumbers: List<String>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return employeeNumbers.distinct().mapNotNull { employeeNumber ->
            networkApi.getEmployee(employeeNumber, avatar = true).firstOrNull()
        }
    }

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
        networkApi.patchTask(taskId, task)
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        return networkApi.createTask(task)
    }
}