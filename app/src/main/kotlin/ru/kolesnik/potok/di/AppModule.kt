package ru.kolesnik.potok.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.datasource.repository.FlowRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.FullProjectRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.LifeAreaRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.TaskRepositoryImpl
import ru.kolesnik.potok.core.network.repository.FlowRepository
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFullProjectRepository(impl: FullProjectRepositoryImpl): FullProjectRepository = impl
    
    @Provides
    @Singleton
    fun provideLifeAreaRepository(impl: LifeAreaRepositoryImpl): LifeAreaRepository = impl
    
    @Provides
    @Singleton
    fun provideFlowRepository(impl: FlowRepositoryImpl): FlowRepository = impl
    
    @Provides
    @Singleton
    fun provideTaskRepository(impl: TaskRepositoryImpl): TaskRepository = impl
}