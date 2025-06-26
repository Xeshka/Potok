package ru.kolesnik.potok.core.datasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.datasource.repository.ChecklistRepository
import ru.kolesnik.potok.core.datasource.repository.ChecklistRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.LifeAreaRepository
import ru.kolesnik.potok.core.datasource.repository.LifeAreaRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.LifeFlowRepository
import ru.kolesnik.potok.core.datasource.repository.LifeFlowRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.SearchRepository
import ru.kolesnik.potok.core.datasource.repository.SearchRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.SyncRepository
import ru.kolesnik.potok.core.datasource.repository.SyncRepositoryImpl
import ru.kolesnik.potok.core.datasource.repository.TaskRepository
import ru.kolesnik.potok.core.datasource.repository.TaskRepositoryImpl
import ru.kolesnik.potok.core.network.repository.FlowRepository
import ru.kolesnik.potok.core.network.repository.FlowRepositoryImpl
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.FullProjectRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindLifeAreaRepository(impl: LifeAreaRepositoryImpl): LifeAreaRepository
    
    @Binds
    @Singleton
    abstract fun bindLifeFlowRepository(impl: LifeFlowRepositoryImpl): LifeFlowRepository
    
    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
    
    @Binds
    @Singleton
    abstract fun bindChecklistRepository(impl: ChecklistRepositoryImpl): ChecklistRepository
    
    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
    
    @Binds
    @Singleton
    abstract fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository
    
    @Binds
    @Singleton
    abstract fun bindFlowRepository(impl: FlowRepositoryImpl): FlowRepository
    
    @Binds
    @Singleton
    abstract fun bindFullProjectRepository(impl: FullProjectRepositoryImpl): FullProjectRepository
}