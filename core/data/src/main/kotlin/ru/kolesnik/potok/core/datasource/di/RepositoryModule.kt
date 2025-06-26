package ru.kolesnik.potok.core.datasource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.datasource.repository.*
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.repository.FlowRepository
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFullProjectRepository(
        syncFullDataSource: SyncFullDataSource
    ): FullProjectRepository = FullProjectRepositoryImpl(syncFullDataSource)

    @Provides
    @Singleton
    fun provideLifeAreaRepository(
        lifeAreaApi: LifeAreaApi,
        lifeAreaDao: ru.kolesnik.potok.core.database.dao.LifeAreaDao
    ): LifeAreaRepository = LifeAreaRepositoryImpl(lifeAreaApi, lifeAreaDao)

    @Provides
    @Singleton
    fun provideFlowRepository(
        lifeFlowApi: LifeFlowApi,
        lifeFlowDao: ru.kolesnik.potok.core.database.dao.LifeFlowDao
    ): FlowRepository = FlowRepositoryImpl(lifeFlowApi, lifeFlowDao)

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskApi: TaskApi,
        taskDao: ru.kolesnik.potok.core.database.dao.TaskDao,
        taskAssigneeDao: ru.kolesnik.potok.core.database.dao.TaskAssigneeDao,
        checklistTaskDao: ru.kolesnik.potok.core.database.dao.ChecklistTaskDao
    ): TaskRepository = TaskRepositoryImpl(taskApi, taskDao, taskAssigneeDao, checklistTaskDao)

    @Provides
    @Singleton
    fun provideSyncRepository(
        lifeAreaApi: LifeAreaApi,
        db: ru.kolesnik.potok.core.database.AppDatabase
    ): SyncRepository = SyncRepositoryImpl(lifeAreaApi, db)
}