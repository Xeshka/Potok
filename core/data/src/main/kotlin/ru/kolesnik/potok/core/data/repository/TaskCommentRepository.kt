package ru.kolesnik.potok.core.data.repository//// TaskCommentRepository.kt
//package ru.kolesnik.potok.core.datasource.repository
//
//import ru.kolesnik.potok.core.database.dao.TaskCommentDao
//import ru.kolesnik.potok.core.database.entitys.TaskCommentEntity
//import ru.kolesnik.potok.core.network.api.CommentApi
//import ru.kolesnik.potok.core.network.model.api.CommentCreateRq
//import ru.kolesnik.potok.core.network.model.api.CommentDto
//import ru.kolesnik.potok.core.network.model.api.CommentUpdateRq
//import ru.kolesnik.potok.core.network.model.api.TaskCommentDTO
//import ru.kolesnik.potok.core.network.model.api.TaskCommentPageDTO
//import java.time.OffsetDateTime
//import java.util.UUID
//import javax.inject.Inject
//
//interface TaskCommentRepository {
//    suspend fun getCommentsForTask(taskId: UUID): TaskCommentPageDTO
//    suspend fun createComment(request: TaskCommentDTO): UUID
//    suspend fun updateComment(commentId: UUID, request: CommentUpdateRq)
//    suspend fun deleteComment(commentId: UUID)
//    suspend fun getCommentReplies(commentId: UUID): List<TaskCommentEntity>
//    suspend fun syncCommentsForTask(taskId: UUID)
//}
//
//class TaskCommentRepositoryImpl @Inject constructor(
//    private val api: CommentApi,
//    private val commentDao: TaskCommentDao
//) : TaskCommentRepository {
//
//    override suspend fun getCommentsForTask(taskId: UUID): List<TaskCommentEntity> {
//        return commentDao.getByTaskId(taskId)
//    }
//
//    override suspend fun createComment(request: CommentCreateRq): UUID {
//        val response = api.createComment(request)
//        val entity = response.toEntity()
//        commentDao.insert(entity)
//        return entity.id
//    }
//
//    override suspend fun updateComment(commentId: UUID, request: CommentUpdateRq) {
//        val response = api.updateComment(commentId, request)
//        val updatedEntity = response.toEntity()
//        commentDao.update(updatedEntity)
//    }
//
//    override suspend fun deleteComment(commentId: UUID) {
//        api.deleteComment(commentId)
//        commentDao.delete(commentDao.getById(commentId) ?: return)
//    }
//
//    override suspend fun getCommentReplies(commentId: UUID): List<TaskCommentEntity> {
//        return commentDao.getReplies(commentId)
//    }
//
//    override suspend fun syncCommentsForTask(taskId: UUID) {
//        val comments = api.getCommentsForTask(taskId)
//        val replies = comments.flatMap { comment ->
//            api.getCommentReplies(comment.id).also { replies ->
//                // Добавляем replies как дочерние комментарии
//                replies.forEach { it.parentCommentId = comment.id }
//            }
//        }
//
//        val allComments = comments + replies
//        val entities = allComments.map { it.toEntity() }
//
//        // Сохраняем в БД
//        commentDao.deleteByTaskId(taskId)
//        commentDao.insertAll(entities)
//    }
//}
//
//// Мапперы
//private fun CommentDto.toEntity(): TaskCommentEntity = TaskCommentEntity(
//    id = this.id,
//    taskCardId = this.taskId,
//    parentCommentId = this.parentCommentId,
//    owner = this.owner,
//    text = this.text,
//    createdAt = this.createdAt,
//    updatedAt = this.updatedAt
//)
//
//private fun CommentCreateRq.toEntity(): TaskCommentEntity = TaskCommentEntity(
//    id = UUID.randomUUID(), // Временный ID, будет заменен при ответе сервера
//    taskCardId = this.taskId,
//    parentCommentId = this.parentCommentId,
//    owner = this.owner,
//    text = this.text,
//    createdAt = OffsetDateTime.now(),
//    updatedAt = OffsetDateTime.now()
//)