package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val uuid: String,
    val postUuid: String,
    val userUuid: String,
    val text: String,
    val createdAt: Long,
    val parentCommentUuid: String? = null
)
