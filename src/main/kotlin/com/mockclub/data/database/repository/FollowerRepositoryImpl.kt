package org.example.com.mockclub.data.database.repository

import com.mockclub.data.database.table.Users
import com.mockclub.domain.model.User
import com.mockclub.data.database.mapper.toUser
import com.mockclub.data.database.table.Followers
import org.example.com.mockclub.domain.repository.FollowerRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class FollowerRepositoryImpl : FollowerRepository {
    override suspend fun getFollowersCount(userId: String): Int = newSuspendedTransaction {
        Followers.selectAll().where { Followers.followedId eq userId }.count().toInt()
    }

    override suspend fun getFollowingCount(userId: String): Int = newSuspendedTransaction {
        Followers.selectAll().where { Followers.followerId eq userId }.count().toInt()
    }

    override suspend fun getFollowers(userId: String, limit: Int, offset: Int): List<User> = newSuspendedTransaction {
        (Followers innerJoin Users)
            .selectAll().where { Followers.followedId eq userId }
            .limit(limit, offset.toLong())
            .map { it.toUser() }
    }

    override suspend fun getFollowing(userId: String, limit: Int, offset: Int): List<User> = newSuspendedTransaction {
        (Followers innerJoin Users)
            .selectAll().where { Followers.followerId eq userId }
            .limit(limit, offset.toLong())
            .map { it.toUser() }
    }

    override suspend fun followUser(followerId: String, followedId: String): Boolean = newSuspendedTransaction {
        Followers.insertIgnore {
            it[this.followerId] = followerId
            it[this.followedId] = followedId
            it[this.createdAt] = System.currentTimeMillis()
        }.insertedCount > 0
    }

    override suspend fun unfollowUser(followerId: String, followedId: String): Boolean = newSuspendedTransaction {
        Followers.deleteWhere {
            (Followers.followerId eq followerId) and (Followers.followedId eq followedId)
        } > 0
    }

    override suspend fun isFollowing(userId: String, targetId: String): Boolean = newSuspendedTransaction {
        Followers.selectAll().where {
            (Followers.followerId eq userId) and (Followers.followedId eq targetId)
        }.limit(1).any()
    }
}
