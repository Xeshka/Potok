package ru.kolesnik.potok.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.database.entitys.*

@Database(
    entities = [
        LifeAreaEntity::class,
        LifeFlowEntity::class,
        TaskEntity::class,
        TaskAssigneeEntity::class,
        ChecklistTaskEntity::class,
        TaskCommentEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lifeAreaDao(): LifeAreaDao
    abstract fun lifeFlowDao(): LifeFlowDao
    abstract fun taskDao(): TaskDao
    abstract fun taskAssigneeDao(): TaskAssigneeDao
    abstract fun checklistTaskDao(): ChecklistTaskDao
    abstract fun taskCommentDao(): TaskCommentDao

    companion object {
        const val DATABASE_NAME = "potok-db"
    }
}