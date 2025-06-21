package ru.kolesnik.potok.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.database.dao.ChecklistDao
import ru.kolesnik.potok.core.database.dao.CommentDao
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesChecklistTaskDao(
        database: AppDatabase,
    ): ChecklistDao = database.checklistTaskDao()

    @Provides
    fun providesCommentDao(
        database: AppDatabase,
    ): CommentDao = database.commentDao()

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
}