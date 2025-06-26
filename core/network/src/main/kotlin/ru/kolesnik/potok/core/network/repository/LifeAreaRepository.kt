package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): String
    suspend fun updateLifeArea(id: String, title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean)
    suspend fun deleteLifeArea(id: String)
}