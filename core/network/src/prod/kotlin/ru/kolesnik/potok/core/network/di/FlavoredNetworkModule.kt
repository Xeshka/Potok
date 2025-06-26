package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.AddressbookSource
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.retrofit.RetrofitNetworkFull
import ru.kolesnik.potok.core.network.retrofit.RetrofitSyncFullDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetworkFull): AddressbookSource

    @Binds
    fun bindsSyncFullDataSource(impl: RetrofitSyncFullDataSource): SyncFullDataSource
}