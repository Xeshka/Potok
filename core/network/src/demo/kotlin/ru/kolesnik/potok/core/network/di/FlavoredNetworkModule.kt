package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.demo.*
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
    
    @Binds
    @Singleton
    fun bindCommentApi(impl: DemoCommentApi): CommentApi
    
    @Binds
    @Singleton
    fun bindBoardApi(impl: DemoBoardApi): BoardApi
}