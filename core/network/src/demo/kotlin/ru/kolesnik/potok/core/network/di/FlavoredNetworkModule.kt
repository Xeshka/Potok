package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.demo.DemoLifeAreaApi
import ru.kolesnik.potok.core.network.demo.DemoSyncFullDataSource
import ru.kolesnik.potok.core.network.retrofit.LifeAreaApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    @Singleton
    fun bindSyncDataSource(impl: DemoSyncFullDataSource): SyncFullDataSource

    // Добавляем биндинг для новой демо-реализации LifeAreaApi
    @Binds
    @Singleton
    fun bindLifeAreaApi(impl: DemoLifeAreaApi): LifeAreaApi
}