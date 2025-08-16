package org.example.com.mockclub.domain.model.composite

import com.mockclub.domain.model.Post
import com.mockclub.domain.model.Tag
import com.mockclub.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class PostWithExtras(
    val post: Post,
    val user: User,
    val images: List<String>,
    val tags: List<Tag>,
    val likeCount: Int,
    val commentCount: Int,
    val likedByCurrentUser: Boolean
)
