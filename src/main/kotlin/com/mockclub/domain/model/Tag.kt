package org.example.com.mockclub.data.database.models

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val usageCount: Int
)
