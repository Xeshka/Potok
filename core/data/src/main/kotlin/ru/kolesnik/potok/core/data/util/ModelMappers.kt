package ru.kolesnik.potok.core.data.util

import ru.kolesnik.potok.core.model.*
import ru.kolesnik.potok.core.network.model.api.*

// Model -> DTO mappers для создания запросов
fun TaskComment.toCreateRequest(): TaskCommentRq {
    return TaskCommentRq(
        text = this.text,
        parentCommentId = this.parentCommentId
    )
}

fun ChecklistTask.toCreateRequest(): ChecklistTaskTitleRq {
    return ChecklistTaskTitleRq(title = this.title)
}