package com.google.samples.apps.nowinandroid.core.analytics

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import ru.kolesnik.potok.core.analytics.StubAnalyticsHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    
    @Binds
    @Singleton
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: SafeFirebaseAnalyticsHelper): AnalyticsHelper

    companion object {
        @Provides
        @Singleton
        fun provideSafeFirebaseAnalyticsHelper(): SafeFirebaseAnalyticsHelper {
            return SafeFirebaseAnalyticsHelper()
        }
    }
}