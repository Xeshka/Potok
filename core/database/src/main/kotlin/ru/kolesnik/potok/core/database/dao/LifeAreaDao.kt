package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity
import ru.kolesnik.potok.core.database.entitys.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.entitys.LifeAreaSharedInfoRecipientEntity
import java.util.UUID

@Dao
interface LifeAreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(area: LifeAreaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(areas: List<LifeAreaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSharedInfo(sharedInfo: LifeAreaSharedInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSharedInfoRecipients(recipients: List<LifeAreaSharedInfoRecipientEntity>)

    @Update
    suspend fun update(area: LifeAreaEntity)

    @Delete
    suspend fun delete(area: LifeAreaEntity)

    @Query("DELETE FROM life_areas")
    suspend fun deleteAll()

    @Query("DELETE FROM life_area_shared_info WHERE lifeAreaId = :lifeAreaId")
    suspend fun deleteSharedInfoByAreaId(lifeAreaId: UUID)

    @Query("DELETE FROM life_area_shared_info_recipients WHERE sharedInfoId = :sharedInfoId")
    suspend fun deleteSharedInfoRecipients(sharedInfoId: UUID)

    @Query("SELECT * FROM life_areas WHERE id = :id")
    suspend fun getById(id: UUID): LifeAreaEntity?

    @Query("SELECT * FROM life_areas ORDER BY placement ASC")
    fun getAll(): Flow<List<LifeAreaEntity>>

    @Query("SELECT * FROM life_areas WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultArea(): LifeAreaEntity?

    @Query("SELECT COUNT(*) FROM life_areas")
    suspend fun count(): Int

    @Query("SELECT * FROM life_areas WHERE isTheme = :isTheme")
    suspend fun getByTheme(isTheme: Boolean): List<LifeAreaEntity>

    @Query("SELECT * FROM life_area_shared_info WHERE lifeAreaId = :lifeAreaId")
    suspend fun getSharedInfo(lifeAreaId: UUID): LifeAreaSharedInfoEntity?

    @Query("SELECT * FROM life_area_shared_info_recipients WHERE sharedInfoId = :sharedInfoId")
    suspend fun getSharedInfoRecipients(sharedInfoId: UUID): List<LifeAreaSharedInfoRecipientEntity>
}