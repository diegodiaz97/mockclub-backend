package org.example.com.mockclub.domain.model.composite

import com.mockclub.domain.model.Comment
import com.mockclub.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class CommentWithExtras(
    val comment: Comment,
    val user: User,
    val likeCount: Int,
    val likedByCurrentUser: Boolean,
    val replies: List<CommentWithExtras> = emptyList(),
    val replyCount: Int,
)
