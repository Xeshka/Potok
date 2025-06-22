package ru.kolesnik.potok.core.datasource.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.datasource.repository.*
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.api.CommentApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // Репозиторий для LifeArea
    @Provides
    @Singleton
    fun provideLifeAreaRepository(
        api: LifeAreaApi,
        lifeAreaDao: LifeAreaDao,
        lifeFlowDao: LifeFlowDao,
        taskDao: TaskDao
    ): LifeAreaRepository = LifeAreaRepositoryImpl(api, lifeAreaDao, lifeFlowDao, taskDao)

    // Репозиторий для LifeFlow
    @Provides
    @Singleton
    fun provideLifeFlowRepository(
        api: LifeFlowApi,
        lifeFlowDao: LifeFlowDao,
        taskDao: TaskDao
    ): LifeFlowRepository = LifeFlowRepositoryImpl(api, lifeFlowDao, taskDao)

    // Репозиторий для задач
    @Provides
    @Singleton
    fun provideTaskRepository(
        api: TaskApi,
        taskDao: TaskDao,
        taskAssigneeDao: TaskAssigneeDao,
        checklistTaskDao: ChecklistTaskDao,
        taskCommentDao: TaskCommentDao
    ): TaskRepository = TaskRepositoryImpl(api, taskDao, taskAssigneeDao, checklistTaskDao, taskCommentDao)

    // Репозиторий для чек-листов
    @Provides
    @Singleton
    fun provideChecklistRepository(
        api: ChecklistApi,
        checklistTaskDao: ChecklistTaskDao
    ): ChecklistRepository = ChecklistRepositoryImpl(api, checklistTaskDao)

    // Репозиторий для комментариев
/*    @Provides
    @Singleton
    fun provideCommentRepository(
        api: CommentApi, // Предполагается, что у вас есть API для комментариев
        taskCommentDao: TaskCommentDao
    ): TaskCommentRepository = TaskCommentRepositoryImpl(api, taskCommentDao)*/

    // Репозиторий для поиска
    @Provides
    @Singleton
    fun provideSearchRepository(
        api: SearchApi
    ): SearchRepository = SearchRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSyncRepository(
        api: LifeAreaApi,
        db: AppDatabase
    ): SyncRepository = SyncRepositoryImpl(api, db)
}