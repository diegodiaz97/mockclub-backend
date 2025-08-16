package org.example.com.mockclub.data.database.repository

import com.mockclub.data.database.mapper.toComment
import com.mockclub.data.database.mapper.toUser
import com.mockclub.data.database.table.CommentLikes
import com.mockclub.data.database.table.Comments
import com.mockclub.data.database.table.Users
import com.mockclub.domain.model.Comment
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.model.composite.CommentWithExtras
import org.example.com.mockclub.domain.repository.CommentRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class CommentRepositoryImpl : CommentRepository {
    override suspend fun getCommentsForPost(
        currentUserId: String,
        postId: String,
        limit: Int,
        cursorCreatedAt: Long?,
    ): List<CommentWithExtras> {
        return newSuspendedTransaction {
            val query = Comments
                .selectAll().where {
                    (Comments.postId eq postId) and
                            (Comments.parentCommentId.isNull()) and
                            (cursorCreatedAt?.let { Comments.createdAt less it } ?: Op.TRUE)
                }
                .orderBy(Comments.createdAt, SortOrder.DESC)
                .limit(limit)

            query.map { row ->
                val comment = row.toComment()

                // Usuario del comentario
                val user = Users.selectAll().where { Users.id eq comment.userId }
                    .firstOrNull()?.toUser() ?: error("User not found")

                // Likes del comentario
                val likeCount = CommentLikes.selectAll()
                    .where { CommentLikes.commentId eq comment.id }
                    .count()

                val likedByCurrentUser = CommentLikes.selectAll().where {
                    (CommentLikes.commentId eq comment.id) and
                            (CommentLikes.userId eq currentUserId)
                }.any()

                // Contar replies (no traerlas)
                val replyCount = Comments
                    .select { Comments.parentCommentId eq comment.id }
                    .count()

                CommentWithExtras(
                    comment = comment,
                    user = user,
                    likeCount = likeCount.toInt(),
                    likedByCurrentUser = likedByCurrentUser,
                    replies = emptyList(),
                    replyCount = replyCount.toInt()
                )
            }
        }
    }

    override suspend fun getRepliesForComment(
        currentUserId: String,
        parentCommentId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<CommentWithExtras> {
        return newSuspendedTransaction {
            Comments
                .selectAll()
                .where {
                    (Comments.parentCommentId eq parentCommentId) and
                            (cursorCreatedAt?.let { Comments.createdAt less it } ?: Op.TRUE)
                }
                .orderBy(Comments.createdAt, SortOrder.DESC)
                .limit(limit)
                .map { row ->
                    val reply = row.toComment()
                    val user = Users.selectAll().where { Users.id eq reply.userId }
                        .first().toUser()
                    val likeCount = CommentLikes
                        .selectAll()
                        .where { CommentLikes.commentId eq reply.id }
                        .count()
                    val likedByCurrentUser = CommentLikes
                        .selectAll()
                        .where {
                            (CommentLikes.commentId eq reply.id) and
                                    (CommentLikes.userId eq currentUserId)
                        }
                        .any()
                    CommentWithExtras(
                        comment = reply,
                        user = user,
                        likeCount = likeCount.toInt(),
                        likedByCurrentUser = likedByCurrentUser,
                        replies = emptyList(),
                        replyCount = 0
                    )
                }
        }
    }

    override suspend fun createComment(comment: Comment): Boolean = newSuspendedTransaction {
        val inserted = Comments.insert {
            it[id] = comment.id
            it[postId] = comment.postId
            it[userId] = comment.userId
            it[text] = comment.text
            it[createdAt] = comment.createdAt
            it[parentCommentId] = comment.parentCommentId
            it[repliesCount] = 0 // nuevo comentario empieza con 0 replies
        }.insertedCount > 0

        if (inserted && comment.parentCommentId != null) {
            Comments.update({ Comments.id eq comment.parentCommentId }) {
                with(SqlExpressionBuilder) {
                    it.update(repliesCount, repliesCount + 1)
                }
            }
        }

        inserted
    }

    override suspend fun likeComment(commentId: String, userId: String): Boolean = newSuspendedTransaction {
        CommentLikes.insertIgnore {
            it[this.commentId] = commentId
            it[this.userId] = userId
        }.insertedCount > 0
    }

    override suspend fun unlikeComment(commentId: String, userId: String): Boolean = newSuspendedTransaction {
        CommentLikes.deleteWhere {
            (CommentLikes.commentId eq commentId) and (CommentLikes.userId eq userId)
        } > 0
    }

    override suspend fun getCommentLikes(commentId: String, limit: Int, offset: Int): List<User> =
        newSuspendedTransaction {
            (CommentLikes innerJoin Users)
                .selectAll().where { CommentLikes.commentId eq commentId }
                .limit(limit, offset.toLong())
                .map { it.toUser() }
        }

    override suspend fun editComment(commentId: String, userId: String, text: String): Boolean =
        newSuspendedTransaction {
            Comments.update({ (Comments.id eq commentId) and (Comments.userId eq userId) }) {
                it[Comments.text] = text
            } > 0
        }

    override suspend fun deleteComment(commentId: String, userId: String): Boolean = newSuspendedTransaction {
        // Primero obtenemos el comentario a eliminar
        val comment = Comments.selectAll()
            .where { (Comments.id eq commentId) and (Comments.userId eq userId) }
            .firstOrNull()?.toComment() ?: return@newSuspendedTransaction false

        if (comment.parentCommentId != null) {
            // Es un reply → decrementamos repliesCount del padre
            Comments.update({ Comments.id eq comment.parentCommentId }) {
                with(SqlExpressionBuilder) {
                    it.update(repliesCount, repliesCount - 1)
                }
            }

            // Eliminamos el reply
            Comments.deleteWhere { Comments.id eq commentId } > 0
        } else {
            // Es un comentario padre → eliminamos sus replies primero
            Comments.deleteWhere { Comments.parentCommentId eq comment.id }

            // Luego eliminamos el comentario padre
            Comments.deleteWhere { Comments.id eq comment.id } > 0
        }
    }
}
