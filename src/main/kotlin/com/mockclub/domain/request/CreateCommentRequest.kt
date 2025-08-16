package org.example.com.mockclub.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentRequest(
    val postId: String,
    val text: String,
    val parentCommentId: String? = null
)
