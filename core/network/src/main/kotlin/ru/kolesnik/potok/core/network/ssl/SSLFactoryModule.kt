package ru.kolesnik.potok.core.network.ssl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SSLFactoryModule {
    @Binds
    fun binds(impl: MtlsSSLFactory): AppSSLFactory
}