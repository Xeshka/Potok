package ru.kolesnik.potok.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea
import java.util.UUID

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>
    suspend fun createLifeArea(title: String, style: String?, isTheme: Boolean, onlyPersonal: Boolean): UUID
    suspend fun updateLifeArea(id: UUID, title: String, style: String?)
    suspend fun deleteLifeArea(id: UUID)
    suspend fun syncLifeAreas()
}