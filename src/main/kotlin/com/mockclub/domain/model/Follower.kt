package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Follower(
    val followerId: String,
    val followedId: String,
    val createdAt: Long
)
