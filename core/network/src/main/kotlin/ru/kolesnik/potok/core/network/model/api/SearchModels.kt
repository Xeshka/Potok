package ru.kolesnik.potok.core.network.model.api

import kotlinx.serialization.Serializable
import ru.kolesnik.potok.core.network.model.UuidSerializer
import java.util.UUID

// Request models
@Serializable
data class SearchQuery1(
    val query: String
)

// Response models
@Serializable
data class SearchResult(
    val tasks: List<TaskSearchResult>,
    val comments: List<CommentSearchResult>,
    val checks: List<CheckSearchResult>
)

@Serializable
data class TaskSearchResult(
    val taskId: TaskId,
    val title: String,
    val description: String? = null
)

@Serializable
data class CommentSearchResult(
    val taskId: TaskId,
    val taskTitle: String,
    val commentId: UUID,
    val commentText: String,
    val commentOwner: String
)

@Serializable
data class CheckSearchResult(
    val taskId: TaskId,
    val taskTitle: String,
    val checkTaskId: UUID,
    val title: String
)

@Serializable
data class TaskId(
    val id: Long? = null,
    val externalId: String
)