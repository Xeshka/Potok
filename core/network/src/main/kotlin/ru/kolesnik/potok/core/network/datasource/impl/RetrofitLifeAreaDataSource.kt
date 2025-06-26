package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.api.LifeAreaApi
import ru.kolesnik.potok.core.network.datasource.LifeAreaDataSource
import ru.kolesnik.potok.core.network.model.api.*
import java.util.UUID
import javax.inject.Inject

class RetrofitLifeAreaDataSource @Inject constructor(
    private val api: LifeAreaApi
) : LifeAreaDataSource {
    override suspend fun getLifeAreas() = api.getLifeAreas()
    override suspend fun createLifeArea(request: LifeAreaRq) = api.createLifeArea(request)
    override suspend fun collocateLifeAreas(lifeAreaIds: List<UUID>) = api.collocateLifeAreas(lifeAreaIds)
    override suspend fun getFullLifeAreas() = api.getFullLifeAreas()
    override suspend fun moveLifeArea(request: LifeAreaMoveDTO) = api.moveLifeArea(request)
    override suspend fun updateLifeArea(id: UUID, request: LifeAreaRq) = api.updateLifeArea(id, request)
    override suspend fun deleteLifeArea(id: UUID) = api.deleteLifeArea(id)
}