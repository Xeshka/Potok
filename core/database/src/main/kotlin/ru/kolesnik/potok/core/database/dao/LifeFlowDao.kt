package ru.kolesnik.potok.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.model.LifeFlowEntity

// LifeFlowDao.kt
@Dao
interface LifeFlowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFlow(flow: LifeFlowEntity)

    @Delete
    suspend fun deleteFlow(flow: LifeFlowEntity)

    @Query("SELECT * FROM life_flows WHERE area_id = :areaId ORDER BY placement ASC")
    fun getFlowsForArea(areaId: String): Flow<List<LifeFlowEntity>>

    @Query("SELECT * FROM life_flows WHERE id = :flowId")
    suspend fun getFlowById(flowId: String): LifeFlowEntity?

    @Query("DELETE FROM life_flows WHERE area_id = :areaId")
    suspend fun deleteAllFlowsForArea(areaId: String)
}