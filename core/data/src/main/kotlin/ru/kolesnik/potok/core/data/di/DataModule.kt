package ru.kolesnik.potok.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.data.repository.FlowRepository
import ru.kolesnik.potok.core.data.repository.FullProjectRepository
import ru.kolesnik.potok.core.data.repository.LifeAreaRepository
import ru.kolesnik.potok.core.data.repository.TaskRepository
import ru.kolesnik.potok.core.datasource.repository.FlowRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.FullProjectRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.LifeAreaRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.TaskRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindLifeAreaRepository(
        lifeAreaRepositoryImpl: LifeAreaRepositoryImpl
    ): LifeAreaRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindFlowRepository(
        flowRepositoryImpl: FlowRepositoryImpl
    ): FlowRepository

    @Binds
    @Singleton
    abstract fun bindFullProjectRepository(
        fullProjectRepositoryImpl: FullProjectRepositoryImpl
    ): FullProjectRepository
}