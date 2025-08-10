package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class CommentLike(
    val userUuid: String,
    val commentUuid: String
)
