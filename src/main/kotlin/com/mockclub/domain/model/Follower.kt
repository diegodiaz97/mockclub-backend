package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Follower(
    val followerUuid: String,
    val followedUuid: String
)
