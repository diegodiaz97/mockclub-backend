package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val uuid: String,
    val userUuid: String,
    val createdAt: Long,
    val text: String,
    val brand: String,
    val team: String,
    val ratio: Float
)
