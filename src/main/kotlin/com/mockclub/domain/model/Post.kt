package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val createdAt: Long,
    val text: String,
    val brand: String,
    val team: String,
    val ratio: Float
)
