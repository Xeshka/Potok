package ru.kolesnik.potok.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.demo.DemoSyncFullDataSource
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
    fun provideFullProjectRepository(repository: FullProjectRepository): FullProjectRepository {
        return repository
    }
    
    @Provides
    @Singleton
    fun provideLifeAreaRepository(repository: LifeAreaRepository): LifeAreaRepository {
        return repository
    }
    
    @Provides
    @Singleton
    fun provideFlowRepository(repository: FlowRepository): FlowRepository {
        return repository
    }
    
    @Provides
    @Singleton
    fun provideTaskRepository(repository: TaskRepository): TaskRepository {
        return repository
    }
}