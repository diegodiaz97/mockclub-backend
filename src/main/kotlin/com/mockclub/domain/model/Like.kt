package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Like(
    val userUuid: String,
    val postUuid: String
)
