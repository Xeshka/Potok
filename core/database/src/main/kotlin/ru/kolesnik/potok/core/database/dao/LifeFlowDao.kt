package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.network.model.api.FlowStatus
import ru.kolesnik.potok.core.database.entitys.LifeFlowEntity
import java.util.UUID

@Dao
interface LifeFlowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flow: LifeFlowEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flows: List<LifeFlowEntity>)

    @Update
    suspend fun update(flow: LifeFlowEntity)

    @Delete
    suspend fun delete(flow: LifeFlowEntity)

    @Query("DELETE FROM life_flows WHERE areaId = :areaId")
    suspend fun deleteByAreaId(areaId: UUID)

    @Query("DELETE FROM life_flows")
    suspend fun deleteAll()

    @Query("SELECT * FROM life_flows WHERE id = :id")
    suspend fun getById(id: UUID): LifeFlowEntity?

    @Query("SELECT * FROM life_flows WHERE areaId = :areaId ORDER BY placement ASC")
    fun getByAreaId(areaId: UUID): Flow<List<LifeFlowEntity>>

    @Query("SELECT * FROM life_flows WHERE status = :status")
    suspend fun getByStatus(status: FlowStatus): List<LifeFlowEntity>

    @Query("SELECT * FROM life_flows WHERE areaId = :areaId AND status = :status")
    suspend fun getByAreaAndStatus(areaId: UUID, status: FlowStatus): List<LifeFlowEntity>

    @Query("UPDATE life_flows SET placement = :newPosition WHERE id = :flowId")
    suspend fun updatePosition(flowId: UUID, newPosition: Int)
}