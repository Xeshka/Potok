package ru.kolesnik.potok.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import ru.kolesnik.potok.core.analytics.NoOpAnalyticsHelper
import ru.kolesnik.potok.core.network.network.AppDispatchers
import ru.kolesnik.potok.core.network.network.Dispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun providesApplicationScope(
        @Dispatcher(AppDispatchers.Default) dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    @Provides
    @Singleton
    fun providesAnalyticsHelper(): AnalyticsHelper {
        // В демо-режиме используем NoOp реализацию
        // В продакшене здесь будет Firebase или другая реализация
        return NoOpAnalyticsHelper()
    }

    @Provides
    @Singleton
    fun providesApplicationContext(
        @ApplicationContext context: Context
    ): Context = context
}