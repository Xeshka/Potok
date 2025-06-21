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
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
import ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea
import ru.kolesnik.potok.core.network.model.potok.NetworkTask
import ru.kolesnik.potok.core.network.model.potok.PatchPayload
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import java.io.BufferedReader
import javax.inject.Inject

class DemoSyncFullDataSource @Inject constructor(
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DemoAssetManager = JvmUnitTestDemoAssetManager,
) : SyncFullDataSource {

    /**
     * Get data from the given JSON [fileName].
     */
    @OptIn(ExperimentalSerializationApi::class)
    private suspend inline fun <reified T> getDataFromJsonFile(fileName: String): List<T> =
        withContext(ioDispatcher) {
            assets.open(fileName).use { inputStream ->
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    inputStream.bufferedReader().use(BufferedReader::readText)
                        .let(networkJson::decodeFromString)
                } else {
                    networkJson.decodeFromStream(inputStream)
                }
            }
        }

    companion object {
        private const val FULL = "full.json"
        private const val EMPLOYEE = "employee.json"

    }

    override suspend fun getFull(): List<NetworkLifeArea> =
        getDataFromJsonFile(FULL)

    override suspend fun getEmployee(
        employeeNumbers: List<EmployeeId>,
        avatar: Boolean
    ): List<EmployeeResponse> = getDataFromJsonFile(EMPLOYEE)

    override suspend fun patchTask(taskId: TaskExternalId, task: PatchPayload) {
    }

    override suspend fun createTask(task: NetworkCreateTask): NetworkTask {
        val t: List<NetworkLifeArea> = getDataFromJsonFile(FULL)
        return t.first().flows!!.first().tasks!!.first()
    }

}