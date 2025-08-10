package org.example.com.mockclub.domain.service

import com.mockclub.domain.model.User

interface FollowerService {
    suspend fun getFollowersCount(userId: String): Int
    suspend fun getFollowingCount(userId: String): Int
    suspend fun getFollowers(userId: String, limit: Int, offset: Int): List<User>
    suspend fun getFollowing(userId: String, limit: Int, offset: Int): List<User>
    suspend fun followUser(followerId: String, followedId: String): Boolean
    suspend fun unfollowUser(followerId: String, followedId: String): Boolean
    suspend fun isFollowing(userId: String, targetId: String): Boolean
}
