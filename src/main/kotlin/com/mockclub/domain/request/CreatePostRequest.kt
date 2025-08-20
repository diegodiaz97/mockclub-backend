package org.example.com.mockclub.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val text: String,
    val team: String,
    val brand: String,
    val ratio: Float,
    val images: List<String>,
    val teamLogo: String,
    val brandLogo: String,
    val designerLogo: String?,
    val tags: List<String>
)
