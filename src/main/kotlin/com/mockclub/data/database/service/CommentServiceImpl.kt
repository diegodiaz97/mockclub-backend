package org.example.com.mockclub.data.database.service

import com.mockclub.domain.model.Comment
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.model.composite.CommentWithExtras
import org.example.com.mockclub.domain.repository.CommentRepository
import org.example.com.mockclub.domain.service.CommentService

class CommentServiceImpl(
    private val commentRepository: CommentRepository
) : CommentService {

    override suspend fun getCommentsForPost(
        currentUserId: String,
        postId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<CommentWithExtras> {
        val comments = commentRepository.getCommentsForPost(currentUserId, postId, limit, cursorCreatedAt)

        return comments.map { comment ->
            val likedByCurrentUser = commentRepository
                .getCommentLikes(comment.comment.id, 1, 0)
                .any { it.id == currentUserId }

            comment.copy(
                likedByCurrentUser = likedByCurrentUser
            )
        }
    }

    override suspend fun getRepliesForComment(
        currentUserId: String,
        parentCommentId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<CommentWithExtras> {
        val replies = commentRepository.getRepliesForComment(currentUserId, parentCommentId, limit, cursorCreatedAt)

        return replies.map { reply ->
            val likedByCurrentUser = commentRepository
                .getCommentLikes(reply.comment.id, 1, 0)
                .any { it.id == currentUserId }

            reply.copy(likedByCurrentUser = likedByCurrentUser)
        }
    }

    override suspend fun createComment(comment: Comment): Boolean {
        return commentRepository.createComment(comment)
    }

    override suspend fun likeComment(commentId: String, userId: String): Boolean {
        return commentRepository.likeComment(commentId, userId)
    }

    override suspend fun unlikeComment(commentId: String, userId: String): Boolean {
        return commentRepository.unlikeComment(commentId, userId)
    }

    override suspend fun getCommentLikes(commentId: String, limit: Int, offset: Int): List<User> {
        return commentRepository.getCommentLikes(commentId, limit, offset)
    }

    override suspend fun editComment(commentId: String, userId: String, text: String): Boolean {
        return commentRepository.editComment(commentId, userId, text)
    }

    override suspend fun deleteComment(commentId: String, userId: String): Boolean {
        return commentRepository.deleteComment(commentId, userId)
    }
}
