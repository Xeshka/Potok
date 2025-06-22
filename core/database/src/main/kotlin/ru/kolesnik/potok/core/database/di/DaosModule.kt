package ru.kolesnik.potok.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.dao.*

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesChecklistTaskDao(
        database: AppDatabase,
    ): ChecklistTaskDao = database.checklistTaskDao()

    @Provides
    fun providesCommentDao(
        database: AppDatabase,
    ): TaskCommentDao = database.taskCommentDao()

    @Provides
    fun providesLifeAreaDao(
        database: AppDatabase,
    ): LifeAreaDao = database.lifeAreaDao()

    @Provides
    fun providesLifeFlowDao(
        database: AppDatabase,
    ): LifeFlowDao = database.lifeFlowDao()

    @Provides
    fun providesTaskDao(
        database: AppDatabase,
    ): TaskDao = database.taskDao()

    @Provides
    fun providesTaskAssigneeDao(
        database: AppDatabase,
    ): TaskAssigneeDao = database.taskAssigneeDao()
}