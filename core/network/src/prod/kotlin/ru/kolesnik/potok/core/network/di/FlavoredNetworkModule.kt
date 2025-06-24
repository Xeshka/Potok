package ru.kolesnik.potok.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kolesnik.potok.core.network.AddressbookSource
import ru.kolesnik.potok.core.network.retrofit.RetrofitNetworkFull

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun binds(impl: RetrofitNetworkFull): AddressbookSource
}