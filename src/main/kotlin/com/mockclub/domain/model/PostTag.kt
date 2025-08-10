package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class PostTag(
    val postUuid: String,
    val tagName: String
)
