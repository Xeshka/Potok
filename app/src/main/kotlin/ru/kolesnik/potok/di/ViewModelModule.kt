package ru.kolesnik.potok.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    // Здесь можно добавить специфичные для ViewModel зависимости
    // если они понадобятся в будущем
}