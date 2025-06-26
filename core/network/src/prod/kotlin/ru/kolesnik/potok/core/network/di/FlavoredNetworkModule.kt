package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.AddressbookSource
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.retrofit.RetrofitNetworkFull
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetworkFull): AddressbookSource

    companion object {
        @Provides
        @Singleton
        fun provideSyncFullDataSource(
            networkJson: Json,
            callFactory: Call.Factory
        ): SyncFullDataSource {
            return object : SyncFullDataSource {
                private val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.BACKEND_URL)
                    .callFactory(callFactory)
                    .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                    .build()

                override suspend fun getFull(): List<ru.kolesnik.potok.core.network.model.potok.NetworkLifeArea> {
                    // Заглушка для prod версии
                    return emptyList()
                }

                override suspend fun gtFullNew(): List<ru.kolesnik.potok.core.network.model.api.LifeAreaDTO> {
                    // Заглушка для prod версии
                    return emptyList()
                }

                override suspend fun getEmployee(
                    employeeNumbers: List<ru.kolesnik.potok.core.network.model.EmployeeId>,
                    avatar: Boolean
                ): List<ru.kolesnik.potok.core.network.model.employee.EmployeeResponse> {
                    // Заглушка для prod версии
                    return emptyList()
                }

                override suspend fun patchTask(
                    taskId: ru.kolesnik.potok.core.network.model.TaskExternalId,
                    task: ru.kolesnik.potok.core.network.model.potok.PatchPayload
                ) {
                    // Заглушка для prod версии
                }

                override suspend fun createTask(
                    task: ru.kolesnik.potok.core.network.model.potok.NetworkCreateTask
                ): ru.kolesnik.potok.core.network.model.potok.NetworkTask {
                    // Заглушка для prod версии
                    return ru.kolesnik.potok.core.network.model.potok.NetworkTask(
                        id = "stub",
                        title = "Stub Task",
                        taskOwner = "stub",
                        creationDate = java.time.OffsetDateTime.now(),
                        payload = ru.kolesnik.potok.core.network.model.potok.NetworkTaskPayload(),
                        cardId = java.util.UUID.randomUUID()
                    )
                }
            }
        }
    }
}