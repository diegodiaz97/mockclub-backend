package org.example.com.mockclub.domain.service

import com.mockclub.domain.model.Comment
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.model.composite.CommentWithExtras

interface CommentService {
    suspend fun getCommentsForPost(
        currentUserId: String,
        postId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<CommentWithExtras>
    suspend fun getRepliesForComment(
        currentUserId: String,
        parentCommentId: String,
        limit: Int,
        cursorCreatedAt: Long? = null
    ): List<CommentWithExtras>
    suspend fun createComment(comment: Comment): Boolean
    suspend fun likeComment(commentId: String, userId: String): Boolean
    suspend fun unlikeComment(commentId: String, userId: String): Boolean
    suspend fun getCommentLikes(commentId: String, limit: Int, offset: Int): List<User>
    suspend fun editComment(commentId: String, userId: String, text: String): Boolean
    suspend fun deleteComment(commentId: String, userId: String): Boolean
}
