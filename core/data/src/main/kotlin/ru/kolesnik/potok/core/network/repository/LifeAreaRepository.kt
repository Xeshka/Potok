package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.model.LifeArea

interface LifeAreaRepository {
    fun getLifeAreas(): Flow<List<LifeArea>>

}