package org.example.com.mockclub.domain.model.composite

import kotlinx.serialization.Serializable
import com.mockclub.domain.model.Post
import com.mockclub.domain.model.PostImage
import com.mockclub.domain.model.Tag
import com.mockclub.domain.model.User

@Serializable
data class PostWithExtras(
    val post: Post,
    val user: User,
    val images: List<PostImage>,
    val tags: List<Tag>,
    val likeCount: Int,
    val commentCount: Int,
    val likedByCurrentUser: Boolean
)