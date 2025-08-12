package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val id: Int,
    val postId: String,
    val imageUrl: String
)
