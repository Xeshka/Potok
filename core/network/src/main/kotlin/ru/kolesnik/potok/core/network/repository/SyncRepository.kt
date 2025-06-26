package ru.kolesnik.potok.core.network.repository

interface SyncRepository {
    suspend fun syncAll()
    suspend fun syncLifeAreas()
    suspend fun syncTasks()
}