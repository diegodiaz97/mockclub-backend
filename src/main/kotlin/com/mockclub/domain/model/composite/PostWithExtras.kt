package org.example.com.mockclub.domain.model.composite

import kotlinx.serialization.Serializable
import org.example.com.mockclub.data.database.models.Post
import org.example.com.mockclub.data.database.models.PostImage
import org.example.com.mockclub.data.database.models.Tag
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