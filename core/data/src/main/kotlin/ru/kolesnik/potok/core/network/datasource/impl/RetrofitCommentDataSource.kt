package ru.kolesnik.potok.core.network.datasource.impl

import ru.kolesnik.potok.core.network.datasource.CommentDataSource
import ru.kolesnik.potok.core.network.model.api.*
import ru.kolesnik.potok.core.network.model.UUID
import ru.kolesnik.potok.core.network.retrofit.ExtendedRetrofitSyncFull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RetrofitCommentDataSource @Inject constructor(
    private val networkApi: ExtendedRetrofitSyncFull
) : CommentDataSource {

    override suspend fun getTaskComments(taskId: String, limit: Int, offset: Int, sort: String?): TaskCommentPageDTO {
        return networkApi.commentApi.getTaskComments(taskId, limit, offset, sort)
    }

    override suspend fun createComment(taskId: String, request: TaskCommentRq): TaskCommentDTO {
        return networkApi.commentApi.createComment(taskId, request)
    }

    override suspend fun getComment(commentId: UUID): TaskCommentDTO {
        return networkApi.commentApi.getComment(commentId)
    }

    override suspend fun updateComment(commentId: UUID, request: TaskCommentRq): TaskCommentDTO {
        return networkApi.commentApi.updateComment(commentId, request)
    }

    override suspend fun deleteComment(commentId: UUID) {
        networkApi.commentApi.deleteComment(commentId)
    }

    override suspend fun searchComments(taskId: String, query: String): SearchRs {
        return networkApi.commentApi.searchComments(taskId, query)
    }
}