package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.datasource.impl.SyncFullDataSource
import ru.kolesnik.potok.core.network.api.*
import ru.kolesnik.potok.core.network.datasource.*
import ru.kolesnik.potok.core.network.datasource.impl.*
import ru.kolesnik.potok.core.network.datasource.impl.RetrofitSyncFullDataSource

import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun bindSyncFullDataSource(impl: RetrofitSyncFullDataSource): SyncFullDataSource

    @Binds
    fun bindLifeAreaDataSource(impl: RetrofitLifeAreaDataSource): LifeAreaDataSource

    @Binds
    fun bindLifeFlowDataSource(impl: RetrofitLifeFlowDataSource): LifeFlowDataSource

    @Binds
    fun bindTaskDataSource(impl: RetrofitTaskDataSource): TaskDataSource

    @Binds
    fun bindAddressbookDataSource(impl: RetrofitAddressbookDataSource): AddressbookDataSource

    @Binds
    fun bindBoardDataSource(impl: RetrofitBoardDataSource): BoardDataSource

    @Binds
    fun bindChecklistDataSource(impl: RetrofitChecklistDataSource): ChecklistDataSource

    @Binds
    fun bindCommentDataSource(impl: RetrofitCommentDataSource): CommentDataSource

    @Binds
    fun bindSearchDataSource(impl: RetrofitSearchDataSource): SearchDataSource

    companion object {
        // API providers
        @Provides
        @Singleton
        fun provideAddressbookApi(retrofit: Retrofit): AddressbookApi =
            retrofit.create(AddressbookApi::class.java)

        @Provides
        @Singleton
        fun provideBoardApi(retrofit: Retrofit): BoardApi =
            retrofit.create(BoardApi::class.java)

        @Provides
        @Singleton
        fun provideChecklistApi(retrofit: Retrofit): ChecklistApi =
            retrofit.create(ChecklistApi::class.java)

        @Provides
        @Singleton
        fun provideCommentApi(retrofit: Retrofit): CommentApi =
            retrofit.create(CommentApi::class.java)

        @Provides
        @Singleton
        fun provideLifeAreaApi(retrofit: Retrofit): LifeAreaApi =
            retrofit.create(LifeAreaApi::class.java)

        @Provides
        @Singleton
        fun provideLifeFlowApi(retrofit: Retrofit): LifeFlowApi =
            retrofit.create(LifeFlowApi::class.java)

        @Provides
        @Singleton
        fun provideSearchApi(retrofit: Retrofit): SearchApi =
            retrofit.create(SearchApi::class.java)

        @Provides
        @Singleton
        fun provideTaskApi(retrofit: Retrofit): TaskApi =
            retrofit.create(TaskApi::class.java)
    }
}