package org.example.com.mockclub.domain.repository

import com.mockclub.domain.model.Post
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.model.composite.PostWithExtras

interface PostRepository {
    suspend fun createPost(post: Post, images: List<String>, tags: List<String>): Boolean
    suspend fun deletePost(postId: String): Boolean
    suspend fun getPostById(postId: String, currentUserId: String): PostWithExtras?
    suspend fun getFeed(currentUserId: String, limit: Int, cursorCreatedAt: Long?): List<PostWithExtras>
    suspend fun getFeedByUser(currentUserId: String, userId: String, limit: Int, cursorCreatedAt: Long?): List<PostWithExtras>
    suspend fun buildPostWithExtras(postsList: List<Post>, currentUserId: String): List<PostWithExtras>
    suspend fun likePost(postId: String, userId: String): Boolean
    suspend fun unlikePost(postId: String, userId: String): Boolean
    suspend fun getPostLikes(postId: String, limit: Int, offset: Int): List<User>
    suspend fun getPostCountByUser(userId: String): Int
}
