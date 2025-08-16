package org.example.com.mockclub.data.database.service

import com.mockclub.domain.model.Post
import org.example.com.mockclub.domain.model.composite.PostWithExtras
import org.example.com.mockclub.domain.repository.PostRepository
import org.example.com.mockclub.domain.service.PostService

class PostServiceImpl(private val repo: PostRepository) : PostService {
    override suspend fun createPost(userId: String, post: Post, images: List<String>, tags: List<String>) =
        repo.createPost(post.copy(userId = userId), images, tags)

    override suspend fun deletePost(postId: String) = repo.deletePost(postId)

    override suspend fun getFeed(currentUserId: String, limit: Int, cursorCreatedAt: Long?): List<PostWithExtras> {
        return repo.getFeed(currentUserId, limit, cursorCreatedAt)
    }

    override suspend fun getFeedByUser(
        currentUserId: String,
        userId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<PostWithExtras> {
        val posts = repo.getFeedByUser(currentUserId, userId, limit, cursorCreatedAt)
        return posts.map { post ->
            val likedByCurrentUser = repo.getPostLikes(post.post.id, 1, 0)
                .any { it.id == currentUserId }
            post.copy(likedByCurrentUser = likedByCurrentUser)
        }
    }

    override suspend fun getPostById(postId: String, currentUserId: String): PostWithExtras? =
        repo.getPostById(postId, currentUserId)

    override suspend fun likePost(postId: String, userId: String) = repo.likePost(postId, userId)
    override suspend fun unlikePost(postId: String, userId: String) = repo.unlikePost(postId, userId)
    override suspend fun getPostLikes(postId: String, limit: Int, offset: Int) =
        repo.getPostLikes(postId, limit, offset)
}
