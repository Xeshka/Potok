/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.kolesnik.potok.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kolesnik.potok.core.database.dao.*
import ru.kolesnik.potok.core.database.entitys.*

@Database(
    entities = [
        LifeAreaEntity::class,
        LifeAreaSharedInfoEntity::class,
        LifeAreaSharedInfoRecipientEntity::class,
        LifeFlowEntity::class,
        TaskEntity::class,
        TaskPayloadEntity::class,
        TaskAssigneeEntity::class,
        ChecklistTaskEntity::class,
        ChecklistTaskResponsibleEntity::class,
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