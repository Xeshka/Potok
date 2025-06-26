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
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.retrofit.RetrofitNetworkFull
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetworkFull): AddressbookSource

    companion object {
        @Provides
        @Singleton
        fun provideSyncFullDataSource(
            callFactory: Call.Factory,
            networkJson: Json
        ): SyncFullDataSource {
            return Retrofit.Builder()
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
                .create(SyncFullDataSource::class.java)
        }

        @Provides
        @Singleton
        fun provideLifeAreaApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): LifeAreaApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(LifeAreaApi::class.java)
        }

        @Provides
        @Singleton
        fun provideLifeFlowApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): LifeFlowApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(LifeFlowApi::class.java)
        }

        @Provides
        @Singleton
        fun provideTaskApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): TaskApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(TaskApi::class.java)
        }

        @Provides
        @Singleton
        fun provideChecklistApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): ChecklistApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ChecklistApi::class.java)
        }

        @Provides
        @Singleton
        fun provideSearchApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): SearchApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(SearchApi::class.java)
        }

        @Provides
        @Singleton
        fun provideCommentApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): CommentApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(CommentApi::class.java)
        }

        @Provides
        @Singleton
        fun provideBoardApi(
            callFactory: Call.Factory,
            networkJson: Json
        ): BoardApi {
            return Retrofit.Builder()
                .baseUrl(RANDM_BASE_URL)
                .callFactory(callFactory)
                .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(BoardApi::class.java)
        }
    }
}