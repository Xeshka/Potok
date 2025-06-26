package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.AuthApi
import ru.kolesnik.potok.core.network.datasource.AuthDataSource
import javax.inject.Inject

class RetrofitAuthDataSource @Inject constructor(
    private val api: AuthApi
) : AuthDataSource {
    override suspend fun auth() = api.auth()
}