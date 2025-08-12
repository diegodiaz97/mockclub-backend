package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostTag(
    val postId: String,
    val tagName: String
)
