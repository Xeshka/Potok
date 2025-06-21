package ru.kolesnik.potok.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_tasks")
data class ChecklistTaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "task_id")
    val taskId: String,
    val title: String,
    val done: Boolean,
    val placement: Int,
    val deadline: String?
)

@Entity(tableName = "checklist_task_responsibles")
data class ChecklistTaskResponsibleEntity(
    @ColumnInfo(name = "checklist_task_id")
    val checklistTaskId: String,
    @ColumnInfo(name = "employee_id")
    val employeeId: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}