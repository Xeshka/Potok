package ru.kolesnik.potok.core.database.dao

import androidx.room.*
import ru.kolesnik.potok.core.database.entitys.LifeAreaEntity

import java.util.UUID

@Dao
interface LifeAreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(area: LifeAreaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(areas: List<LifeAreaEntity>)

    @Update
    suspend fun update(area: LifeAreaEntity)

    @Delete
    suspend fun delete(area: LifeAreaEntity)

    @Query("DELETE FROM life_areas")
    suspend fun deleteAll()

    @Query("SELECT * FROM life_areas WHERE id = :id")
    suspend fun getById(id: UUID): LifeAreaEntity?

    @Query("SELECT * FROM life_areas ORDER BY placement ASC")
    suspend fun getAll(): List<LifeAreaEntity>

    @Query("SELECT * FROM life_areas WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultArea(): LifeAreaEntity?

    @Query("SELECT COUNT(*) FROM life_areas")
    suspend fun count(): Int

    @Query("SELECT * FROM life_areas WHERE isTheme = :isTheme")
    suspend fun getByTheme(isTheme: Boolean): List<LifeAreaEntity>
}