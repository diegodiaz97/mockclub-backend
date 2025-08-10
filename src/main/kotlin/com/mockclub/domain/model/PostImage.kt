package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val id: Int,
    val postUuid: String,
    val imageUrl: String
)
