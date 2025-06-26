package ru.kolesnik.potok.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.employee.toDomain
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
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
    fun provideFullProjectRepository(
        syncFullDataSource: SyncFullDataSource,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): FullProjectRepository {
        return object : FullProjectRepository {
            override suspend fun sync() {
                syncFullDataSource.getFull()
            }

            override suspend fun getEmployee(employeeIds: List<String>) = 
                syncFullDataSource.getEmployee(employeeIds, true)
        }
    }
}