package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Like(
    val userId: String,
    val postId: String
)
