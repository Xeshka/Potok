package ru.kolesnik.potok.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    
    @Provides
    @Singleton
    fun provideNavigationHelper(): NavigationHelper {
        return NavigationHelper()
    }
}

/**
 * Вспомогательный класс для навигации
 */
class NavigationHelper {
    
    fun navigateToTask(taskId: String): String {
        return "task_detail/$taskId"
    }
    
    fun navigateToLifeArea(areaId: String): String {
        return "life_area/$areaId"
    }
    
    fun navigateBack(): String {
        return "back"
    }
}