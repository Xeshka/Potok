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
import androidx.room.RoomDatabase
import ru.kolesnik.potok.core.database.dao.ChecklistDao
import ru.kolesnik.potok.core.database.dao.CommentDao
import ru.kolesnik.potok.core.database.dao.LifeAreaDao
import ru.kolesnik.potok.core.database.dao.LifeFlowDao
import ru.kolesnik.potok.core.database.dao.TaskDao

import ru.kolesnik.potok.core.database.model.ChecklistTaskEntity
import ru.kolesnik.potok.core.database.model.ChecklistTaskResponsibleEntity
import ru.kolesnik.potok.core.database.model.LifeAreaEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoEntity
import ru.kolesnik.potok.core.database.model.LifeAreaSharedInfoRecipientEntity
import ru.kolesnik.potok.core.database.model.LifeFlowEntity
import ru.kolesnik.potok.core.database.model.TaskAssigneeEntity
import ru.kolesnik.potok.core.database.model.TaskCommentEntity
import ru.kolesnik.potok.core.database.model.TaskEntity
import ru.kolesnik.potok.core.database.model.TaskPayloadEntity

@Database(
    entities = [
        ChecklistTaskEntity::class,
        ChecklistTaskResponsibleEntity::class,
        LifeAreaEntity::class,
        LifeAreaSharedInfoEntity::class,
        LifeAreaSharedInfoRecipientEntity::class,
        LifeFlowEntity::class,
        TaskEntity::class,
        TaskPayloadEntity::class,
        TaskAssigneeEntity::class,
        TaskCommentEntity::class,
    ],
    version = 2,
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun checklistTaskDao(): ChecklistDao
    abstract fun commentDao(): CommentDao
    abstract fun lifeAreaDao(): LifeAreaDao
    abstract fun lifeFlowDao(): LifeFlowDao
    abstract fun taskDao(): TaskDao
}