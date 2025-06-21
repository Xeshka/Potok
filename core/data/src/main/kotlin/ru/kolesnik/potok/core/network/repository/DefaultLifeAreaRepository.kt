package ru.kolesnik.potok.core.network.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.model.LifeArea
import ru.kolesnik.potok.core.network.SyncFullDataSource
import ru.kolesnik.potok.core.network.model.potok.toDomain
import javax.inject.Inject

class DefaultLifeAreaRepository @Inject constructor(
    private val syncFullDataSource: SyncFullDataSource,
    private val lifeAreaDao: LifeAreaDao,
) : LifeAreaRepository {

    override fun getLifeAreas(): Flow<List<LifeArea>> {
        return lifeAreaDao.getAllLifeAreas().map { lifeAreas ->

            lifeAreas.map { lifeArea ->

                val info = lifeAreaDao.getSharedInfoForLifeArea(lifeArea.id)

                val recipients = info?.let {
                    lifeAreaDao.getRecipientsForSharedInfo(it.lifeAreaId).first().toList()
                }

                lifeArea.toDomain(
                    sharedInfoEntity = info,
                    recipientEntities = recipients
                )
            }

        }
    }

}