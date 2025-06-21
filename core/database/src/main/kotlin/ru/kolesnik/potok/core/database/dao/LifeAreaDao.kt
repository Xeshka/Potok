package ru.kolesnik.potok.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.model.LifeAreaEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoRecipientEntity

@Dao
interface LifeAreaDao {
    // LifeAreaEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLifeArea(lifeArea: LifeAreaEntity)

    @Delete
    suspend fun deleteLifeArea(lifeArea: LifeAreaEntity)

    @Query("SELECT * FROM life_areas ORDER BY placement")
    fun getAllLifeAreas(): Flow<List<LifeAreaEntity>>

    @Query("SELECT * FROM life_areas WHERE id = :id")
    suspend fun getLifeAreaById(id: String): LifeAreaEntity?

    // LifeAreaSharedInfoEntity operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSharedInfo(info: LifeAreaSharedInfoEntity)

    @Query("SELECT * FROM life_area_shared_info WHERE life_area_id = :lifeAreaId")
    suspend fun getSharedInfoForLifeArea(lifeAreaId: String): LifeAreaSharedInfoEntity?

    @Delete
    suspend fun deleteSharedInfo(info: LifeAreaSharedInfoEntity)

    // LifeAreaSharedInfoRecipientEntity operations
    @Insert
    suspend fun addSharedInfoRecipient(recipient: LifeAreaSharedInfoRecipientEntity)

    @Query("DELETE FROM life_area_shared_info_recipients WHERE shared_info_id = :sharedInfoId")
    suspend fun deleteAllRecipientsForSharedInfo(sharedInfoId: String)

    @Query("SELECT * FROM life_area_shared_info_recipients WHERE shared_info_id = :sharedInfoId")
    fun getRecipientsForSharedInfo(sharedInfoId: String): Flow<List<LifeAreaSharedInfoRecipientEntity>>
}