package org.example.com.mockclub.data.database.repository

import com.mockclub.data.database.mapper.toPost
import com.mockclub.data.database.mapper.toPostImage
import com.mockclub.data.database.mapper.toTag
import com.mockclub.data.database.mapper.toUser
import com.mockclub.data.database.table.*
import com.mockclub.domain.model.Post
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.model.composite.PostWithExtras
import org.example.com.mockclub.domain.repository.PostRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PostRepositoryImpl : PostRepository {
    override suspend fun createPost(post: Post, images: List<String>, tags: List<String>): Boolean =
        newSuspendedTransaction {
            Posts.insert {
                it[id] = post.id
                it[userId] = post.userId
                it[createdAt] = post.createdAt
                it[text] = post.text
                it[brand] = post.brand
                it[team] = post.team
                it[ratio] = post.ratio
            }

            images.forEach { url ->
                PostImages.insert {
                    it[postId] = post.id
                    it[imageUrl] = url
                }
            }

            tags.forEach { tag ->
                // Insertar tag si no existe (evitar error FK)
                Tags.insertIgnore {
                    it[name] = tag
                }

                // Incrementar usageCount del tag
                Tags.update({ Tags.name eq tag }) {
                    with(SqlExpressionBuilder) {
                        it.update(usageCount, usageCount + 1)
                    }
                }

                // Insertar relación post-tag
                PostTags.insert {
                    it[postId] = post.id
                    it[tagName] = tag
                }
            }

            true
        }

    override suspend fun deletePost(postId: String): Boolean = newSuspendedTransaction {
        // Primero borrar likes
        Likes.deleteWhere { Likes.postId eq postId }

        // Borrar comentarios
        Comments.deleteWhere { Comments.postId eq postId }

        // Borrar imágenes
        PostImages.deleteWhere { PostImages.postId eq postId }

        // Borrar relaciones post-tags
        PostTags.deleteWhere { PostTags.postId eq postId }

        // Finalmente borrar el post
        val deletedCount = Posts.deleteWhere { Posts.id eq postId }

        deletedCount > 0
    }

    override suspend fun getPostById(postId: String, currentUserId: String): PostWithExtras? = newSuspendedTransaction {
        val postRow =
            Posts.selectAll().where { Posts.id eq postId }.singleOrNull() ?: return@newSuspendedTransaction null
        val post = postRow.toPost()

        val userRow =
            Users.selectAll().where { Users.id eq post.userId }.singleOrNull() ?: return@newSuspendedTransaction null
        val user = userRow.toUser()

        val images = PostImages.selectAll().where { PostImages.postId eq post.id }.map { it.toPostImage().imageUrl }
        val tags = PostTags.selectAll().where { PostTags.postId eq post.id }.map { it.toTag() }

        val likeCount = Likes.selectAll().where { Likes.postId eq post.id }.count().toInt()
        val commentCount = Comments.selectAll().where { Comments.postId eq post.id }.count().toInt()

        val likedByCurrentUser = currentUserId.let {
            Likes.selectAll().where { (Likes.postId eq post.id) and (Likes.userId eq it) }.any()
        }

        PostWithExtras(post, user, images, tags, likeCount, commentCount, likedByCurrentUser)
    }

    private suspend fun getPostsFilteredByUser(
        userId: String?, // nullable
        limit: Int,
        cursorCreatedAt: Long?
    ): List<Post> = newSuspendedTransaction {
        val baseQuery = if (userId != null) {
            if (cursorCreatedAt != null) {
                Posts.selectAll().where { (Posts.userId eq userId) and (Posts.createdAt less cursorCreatedAt) }
            } else {
                Posts.selectAll().where { Posts.userId eq userId }
            }
        } else {
            if (cursorCreatedAt != null) {
                Posts.selectAll().where { Posts.createdAt less cursorCreatedAt }
            } else {
                Posts.selectAll()
            }
        }

        baseQuery
            .orderBy(Posts.createdAt, SortOrder.DESC)
            .limit(limit)
            .map { it.toPost() }
    }

    // Ahora el getFeed reutiliza esa función para traer posts
    override suspend fun getFeed(
        currentUserId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<PostWithExtras> {
        val postsList = getPostsFilteredByUser(userId = null, limit = limit, cursorCreatedAt = cursorCreatedAt)
        return buildPostWithExtras(postsList, currentUserId)
    }

    // Y el getFeedByUser llama a la misma función pero pasando userId
    override suspend fun getFeedByUser(
        currentUserId: String,
        userId: String,
        limit: Int,
        cursorCreatedAt: Long?
    ): List<PostWithExtras> {
        val postsList = getPostsFilteredByUser(userId = userId, limit = limit, cursorCreatedAt = cursorCreatedAt)
        return buildPostWithExtras(postsList, currentUserId) // PASAR currentUserId
    }

    // Función que arma la lista de PostWithExtras a partir de posts y currentUserId
    private suspend fun buildPostWithExtras(
        postsList: List<Post>,
        currentUserId: String
    ): List<PostWithExtras> = newSuspendedTransaction {
        if (postsList.isEmpty()) return@newSuspendedTransaction emptyList()

        val postIds = postsList.map { it.id }
        val userIds = postsList.map { it.userId }.distinct()

        val usersMap = Users.select { Users.id inList userIds }
            .associateBy { it[Users.id] }
            .mapValues { it.value.toUser() }

        val imagesMap = PostImages.selectAll().where { PostImages.postId inList postIds }
            .groupBy({ it[PostImages.postId] }, { it[PostImages.imageUrl] })

        val tagsMap = (PostTags innerJoin Tags)
            .selectAll().where { PostTags.postId inList postIds }
            .groupBy({ it[PostTags.postId] }, { it.toTag() })

        val likeCounts = Likes.slice(Likes.postId, Likes.postId.count())
            .selectAll().where { Likes.postId inList postIds }
            .groupBy(Likes.postId)
            .associate { it[Likes.postId] to it[Likes.postId.count()] }

        val commentCounts = Comments.slice(Comments.postId, Comments.postId.count())
            .selectAll().where { Comments.postId inList postIds }
            .groupBy(Comments.postId)
            .associate { it[Comments.postId] to it[Comments.postId.count()] }

        val likedPostIds = Likes.selectAll().where { (Likes.postId inList postIds) and (Likes.userId eq currentUserId) }
            .map { it[Likes.postId] }.toSet()

        postsList.map { post ->
            val user = usersMap[post.userId] ?: error("User not found")
            val images = imagesMap[post.id] ?: emptyList()
            val tags = tagsMap[post.id] ?: emptyList()
            val likeCount = likeCounts[post.id] ?: 0
            val commentCount = commentCounts[post.id] ?: 0
            val likedByCurrentUser = likedPostIds.contains(post.id)

            PostWithExtras(post, user, images, tags, likeCount.toInt(), commentCount.toInt(), likedByCurrentUser)
        }
    }

    override suspend fun likePost(postId: String, userId: String): Boolean = newSuspendedTransaction {
        val exists = Likes.selectAll().where { (Likes.postId eq postId) and (Likes.userId eq userId) }
            .empty().not()

        if (exists) return@newSuspendedTransaction false

        Likes.insert {
            it[this.postId] = postId
            it[this.userId] = userId
        }

        true
    }

    override suspend fun unlikePost(postId: String, userId: String): Boolean = newSuspendedTransaction {
        Likes.deleteWhere { (Likes.postId eq postId) and (Likes.userId eq userId) } > 0
    }

    override suspend fun getPostLikes(postId: String, limit: Int, offset: Int): List<User> = newSuspendedTransaction {
        (Likes innerJoin Users)
            .selectAll().where { Likes.postId eq postId }
            .limit(limit, offset.toLong())
            .map { it.toUser() }
    }
}
