package ru.kolesnik.potok.core.network.api

import retrofit2.http.GET
import retrofit2.http.Headers

interface AuthApi {
    @GET("/")
    @Headers("Accept: text/html")
    suspend fun auth()
}
