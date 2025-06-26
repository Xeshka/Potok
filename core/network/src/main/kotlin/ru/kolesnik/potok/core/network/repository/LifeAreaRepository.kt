package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea
import java.util.UUID

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): UUID
    suspend fun updateLifeArea(id: UUID, title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): LifeArea
    suspend fun deleteLifeArea(id: UUID)
}