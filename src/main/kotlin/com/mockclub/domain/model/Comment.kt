package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val text: String,
    val createdAt: Long,
    val parentCommentId: String? = null
)
