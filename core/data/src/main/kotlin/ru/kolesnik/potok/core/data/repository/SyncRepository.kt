package ru.kolesnik.potok.core.data.repository

import ru.kolesnik.potok.core.network.result.Result

interface SyncRepository {
    suspend fun syncAll(): Result<Unit>
    suspend fun syncFullProject(): Result<Unit>
}