package ru.kolesnik.potok.core.datasource.repository

import ru.kolesnik.potok.core.database.AppDatabase
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val dataSource: SyncFullDataSource,
    private val database: AppDatabase
) : SyncRepository {

    override suspend fun syncAll() {
        syncLifeAreas()
        syncTasks()
    }

    override suspend fun syncLifeAreas() {
        try {
            val lifeAreas = dataSource.gtFullNew()
            // Здесь можно добавить логику сохранения в БД
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }

    override suspend fun syncTasks() {
        try {
            val fullData = dataSource.getFull()
            // Здесь можно добавить логику сохранения в БД
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }
}