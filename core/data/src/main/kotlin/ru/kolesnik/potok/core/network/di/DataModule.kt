package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.repository.DefaultFlowRepository
import ru.kolesnik.potok.core.network.repository.DefaultFullProjectRepository
import ru.kolesnik.potok.core.network.repository.DefaultLifeAreaRepository
import ru.kolesnik.potok.core.network.repository.DefaultTaskRepository
import ru.kolesnik.potok.core.network.repository.FlowRepository
import ru.kolesnik.potok.core.network.repository.FullProjectRepository
import ru.kolesnik.potok.core.network.repository.LifeAreaRepository
import ru.kolesnik.potok.core.network.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsFullProjectRepository(
        fullProjectRepository: DefaultFullProjectRepository,
    ): FullProjectRepository

    @Binds
    internal abstract fun bindsLifeAreaRepository(
        lifeAreaRepository: DefaultLifeAreaRepository,
    ): LifeAreaRepository

    @Binds
    internal abstract fun bindsTaskRepository(
        taskRepository: DefaultTaskRepository,
    ): TaskRepository

    @Binds
    internal abstract fun bindsFlowRepository(
        flowRepository: DefaultFlowRepository,
    ): FlowRepository
}