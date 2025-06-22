package ru.kolesnik.potok.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.retrofit.CommentApi

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideChecklistApi(retrofit: Retrofit): ChecklistApi {
        return retrofit.create(ChecklistApi::class.java)
    }

    @Provides
    fun provideLifeAreaApi(retrofit: Retrofit): LifeAreaApi {
        return retrofit.create(LifeAreaApi::class.java)
    }

    @Provides
    fun provideLifeFlowApi(retrofit: Retrofit): LifeFlowApi {
        return retrofit.create(LifeFlowApi::class.java)
    }

    @Provides
    fun provideBoardApi(retrofit: Retrofit): BoardApi {
        return retrofit.create(BoardApi::class.java)
    }

    @Provides
    fun provideTaskApi(retrofit: Retrofit): TaskApi {
        return retrofit.create(TaskApi::class.java)
    }

    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }

    @Provides
    fun provideCommentApi(retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
    }
}