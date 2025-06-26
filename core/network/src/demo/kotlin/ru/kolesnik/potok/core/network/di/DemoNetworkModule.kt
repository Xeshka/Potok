package ru.kolesnik.potok.core.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import ru.kolesnik.potok.core.network.demo.DemoAssetManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DemoNetworkModule {

    @Provides
    @Singleton
    fun providesDemoAssetManager(
        @ApplicationContext context: Context,
    ): DemoAssetManager = DemoAssetManager(context.assets::open)

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
}