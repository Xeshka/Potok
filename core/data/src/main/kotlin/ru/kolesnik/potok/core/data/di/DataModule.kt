package ru.kolesnik.potok.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.data.repository.*
import ru.kolesnik.potok.core.network.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindLifeAreaRepository(
        impl: LifeAreaRepositoryImpl
    ): LifeAreaRepository

    @Binds
    @Singleton
    abstract fun bindFlowRepository(
        impl: FlowRepositoryImpl
    ): FlowRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindFullProjectRepository(
        impl: FullProjectRepositoryImpl
    ): FullProjectRepository
}