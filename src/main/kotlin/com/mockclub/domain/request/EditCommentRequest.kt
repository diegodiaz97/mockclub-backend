package org.example.com.mockclub.domain.request

import kotlinx.serialization.Serializable

@Serializable
data class EditCommentRequest(
    val text: String
)
