package ru.kolesnik.potok.core.network.datasource

interface AuthDataSource {
    suspend fun auth()
}