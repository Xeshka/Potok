package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.retrofit.RetrofitSyncFullDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    @Singleton
    fun bindSyncDataSource(impl: RetrofitSyncFullDataSource): SyncFullDataSource
}