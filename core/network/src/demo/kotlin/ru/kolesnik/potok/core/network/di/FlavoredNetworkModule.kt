package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.api.ChecklistApi
import ru.kolesnik.potok.core.network.api.LifeFlowApi
import ru.kolesnik.potok.core.network.api.SearchApi
import ru.kolesnik.potok.core.network.api.TaskApi
import ru.kolesnik.potok.core.network.demo.DemoChecklistApi
import ru.kolesnik.potok.core.network.demo.DemoLifeAreaApi
import ru.kolesnik.potok.core.network.demo.DemoLifeFlowApi
import ru.kolesnik.potok.core.network.demo.DemoSearchApi
import ru.kolesnik.potok.core.network.demo.DemoSyncFullDataSource
import ru.kolesnik.potok.core.network.demo.DemoTaskApi
import ru.kolesnik.potok.core.network.retrofit.LifeAreaApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    @Singleton
    fun bindSyncDataSource(impl: DemoSyncFullDataSource): SyncFullDataSource

    @Binds
    @Singleton
    fun bindLifeAreaApi(impl: DemoLifeAreaApi): LifeAreaApi

    @Binds
    @Singleton
    fun bindLifeFlowApi(impl: DemoLifeFlowApi): LifeFlowApi

    @Binds
    @Singleton
    fun bindTaskApi(impl: DemoTaskApi): TaskApi

    @Binds
    @Singleton
    fun bindChecklistApi(impl: DemoChecklistApi): ChecklistApi

    @Binds
    @Singleton
    fun bindSearchApi(impl: DemoSearchApi): SearchApi
}