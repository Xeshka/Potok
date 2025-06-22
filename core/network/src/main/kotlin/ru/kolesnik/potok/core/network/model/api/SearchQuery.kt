package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlinx.serialization.Contextual

@Serializable
data class SearchQuery(
    val query: String
)

@Serializable
data class SearchRs(
    @Contextual val commentIds: List<@Contextual UUID>
)

@Serializable
data class SearchResult(
    val tasks: List<TaskSearchResult>,
    val comments: List<CommentSearchResult>,
    val checks: List<CheckSearchResult>
)

@Serializable
data class CommentSearchResult(
    val taskId: TaskId,
    val taskTitle: String,
    @Contextual val commentId: UUID,
    val commentText: String,
    val commentOwner: String
)

@Serializable
data class TaskSearchResult(
    val taskId: TaskId,
    val title: String,
    val description: String? = null
)

@Serializable
data class CheckSearchResult(
    val taskId: TaskId,
    val taskTitle: String,
    @Contextual val checkTaskId: UUID,
    val title: String
)