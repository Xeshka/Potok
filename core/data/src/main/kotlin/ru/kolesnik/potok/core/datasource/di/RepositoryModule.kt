package ru.kolesnik.potok.core.datasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.datasource.repository.*
import ru.kolesnik.potok.core.network.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLifeAreaRepository(
        impl: LifeAreaRepositoryImpl
    ): LifeAreaRepository

    @Binds
    @Singleton
    abstract fun bindLifeFlowRepository(
        impl: LifeFlowRepositoryImpl
    ): LifeFlowRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindChecklistRepository(
        impl: ChecklistRepositoryImpl
    ): ChecklistRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        impl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(
        impl: SyncRepositoryImpl
    ): SyncRepository
}