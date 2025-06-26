package ru.kolesnik.potok.core.datasource.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.datasource.repository.*
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.repository.*
import ru.kolesnik.potok.core.network.SyncFullDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // Репозиторий для LifeArea
    @Provides
    @Singleton
    fun provideLifeAreaRepository(
        dataSource: SyncFullDataSource,
        lifeAreaDao: LifeAreaDao,
        lifeFlowDao: LifeFlowDao,
        taskDao: TaskDao
    ): LifeAreaRepository = LifeAreaRepositoryImpl(dataSource, lifeAreaDao, lifeFlowDao, taskDao)

    // Репозиторий для LifeFlow
    @Provides
    @Singleton
    fun provideLifeFlowRepository(
        dataSource: SyncFullDataSource,
        lifeFlowDao: LifeFlowDao,
        taskDao: TaskDao
    ): FlowRepository = FlowRepositoryImpl(dataSource, lifeFlowDao, taskDao)

    // Репозиторий для задач
    @Provides
    @Singleton
    fun provideTaskRepository(
        dataSource: SyncFullDataSource,
        taskDao: TaskDao,
        taskAssigneeDao: TaskAssigneeDao,
        checklistTaskDao: ChecklistTaskDao,
        taskCommentDao: TaskCommentDao
    ): TaskRepository = TaskRepositoryImpl(dataSource, taskDao, taskAssigneeDao, checklistTaskDao, taskCommentDao)

    // Репозиторий для чек-листов
    @Provides
    @Singleton
    fun provideChecklistRepository(
        api: ChecklistApi,
        checklistTaskDao: ChecklistTaskDao
    ): ChecklistRepository = ChecklistRepositoryImpl(api, checklistTaskDao)

    // Репозиторий для поиска
    @Provides
    @Singleton
    fun provideSearchRepository(
        api: SearchApi
    ): SearchRepository = SearchRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSyncRepository(
        dataSource: SyncFullDataSource,
        db: AppDatabase
    ): SyncRepository = SyncRepositoryImpl(dataSource, db)

    @Provides
    @Singleton
    fun provideFullProjectRepository(
        dataSource: SyncFullDataSource,
        db: AppDatabase
    ): FullProjectRepository = FullProjectRepositoryImpl(dataSource, db)
}