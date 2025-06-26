package ru.kolesnik.potok.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // ✅ Убираем дублирующий провайдер AnalyticsHelper
    // Он уже предоставляется в AnalyticsModule
}