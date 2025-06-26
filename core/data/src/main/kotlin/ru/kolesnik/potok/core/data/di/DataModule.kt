package ru.kolesnik.potok.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindLifeAreaRepository(
        lifeAreaRepositoryImpl: LifeAreaRepositoryImpl
    ): LifeAreaRepository

    @Binds
    abstract fun bindLifeFlowRepository(
        lifeFlowRepositoryImpl: LifeFlowRepositoryImpl
    ): LifeFlowRepository

    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    abstract fun bindChecklistRepository(
        checklistRepositoryImpl: ChecklistRepositoryImpl
    ): ChecklistRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindSyncRepository(
        syncRepositoryImpl: SyncRepositoryImpl
    ): SyncRepository
}