package com.mockclub.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val usageCount: Int
)
