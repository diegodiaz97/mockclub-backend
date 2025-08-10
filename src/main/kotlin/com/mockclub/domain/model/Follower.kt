package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Follower(
    val followerId: String,
    val followedId: String,
    val createdAt: Long
)
