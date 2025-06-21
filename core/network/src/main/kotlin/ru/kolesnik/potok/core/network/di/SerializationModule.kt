package ru.kolesnik.potok.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.network.model.customSerializersModule
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            serializersModule = customSerializersModule
            ignoreUnknownKeys = true // Игнорировать неизвестные ключи
            isLenient = true         // Разрешить нестрогий JSON
            encodeDefaults = true    // Сериализовать значения по умолчанию
        }
    }
}