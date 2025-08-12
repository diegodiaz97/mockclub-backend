package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentLike(
    val userId: String,
    val commentId: String
)
