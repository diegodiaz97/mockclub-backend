package org.example.com.mockclub.data.database.repository

import com.mockclub.data.database.mapper.toPost
import com.mockclub.data.database.table.PostTags
import com.mockclub.data.database.table.Posts
import com.mockclub.data.database.table.Tags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.com.mockclub.domain.model.composite.PostWithExtras
import org.example.com.mockclub.domain.repository.PostRepository
import org.example.com.mockclub.domain.repository.SearchRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class SearchRepositoryImpl(
    private val postRepository: PostRepository
) : SearchRepository {

    private fun Column<String>.lowerCase() = CustomStringFunction("lower", this)

    override suspend fun searchPostsByTeam(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> = withContext(Dispatchers.IO) {
        newSuspendedTransaction {
            val terms = query.split(" ").filter { it.isNotBlank() }

            val condition: Op<Boolean> = terms.fold(Op.FALSE as Op<Boolean>) { acc, term ->
                val pattern = "%${term.lowercase()}%"
                acc or (Posts.team.lowerCase() like pattern)
            }

            val posts = Posts
                .selectAll()
                .where { condition }
                .withDistinct() // 👈 fuerza SELECT DISTINCT
                .limit(limit, offset.toLong())
                .map { it.toPost() }
                .distinctBy { it.id } // 👈 seguridad extra en memoria

            postRepository.buildPostWithExtras(posts, currentUserId)
        }
    }

    override suspend fun searchPostsByBrand(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> = withContext(Dispatchers.IO) {
        newSuspendedTransaction {
            val terms = query.split(" ").filter { it.isNotBlank() }

            val condition: Op<Boolean> = terms.fold(Op.FALSE as Op<Boolean>) { acc, term ->
                val pattern = "%${term.lowercase()}%"
                acc or (Posts.brand.lowerCase() like pattern)
            }

            val posts = Posts
                .selectAll()
                .where { condition }
                .withDistinct()
                .limit(limit, offset.toLong())
                .map { it.toPost() }
                .distinctBy { it.id }

            postRepository.buildPostWithExtras(posts, currentUserId)
        }
    }

    override suspend fun searchPostsByTag(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> = withContext(Dispatchers.IO) {
        newSuspendedTransaction {
            val terms = query.split(" ").filter { it.isNotBlank() }

            val condition: Op<Boolean> = terms.fold(Op.FALSE as Op<Boolean>) { acc, term ->
                val pattern = "%${term.lowercase()}%"
                acc or (Tags.name.lowerCase() like pattern)
            }

            val posts = (Posts innerJoin PostTags innerJoin Tags)
                .slice(Posts.columns)
                .selectAll()
                .where { condition }
                .withDistinct()
                .limit(limit, offset.toLong())
                .map { it.toPost() }
                .distinctBy { it.id }

            postRepository.buildPostWithExtras(posts, currentUserId)
        }
    }
}
