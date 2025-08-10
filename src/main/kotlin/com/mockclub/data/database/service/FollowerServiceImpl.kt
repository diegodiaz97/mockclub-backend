package org.example.com.mockclub.data.database.service

import org.example.com.mockclub.domain.repository.FollowerRepository
import org.example.com.mockclub.domain.service.FollowerService

class FollowerServiceImpl(
    private val followerRepository: FollowerRepository
) : FollowerService {

    override suspend fun getFollowersCount(userId: String) =
        followerRepository.getFollowersCount(userId)

    override suspend fun getFollowingCount(userId: String) =
        followerRepository.getFollowingCount(userId)

    override suspend fun getFollowers(userId: String, limit: Int, offset: Int) =
        followerRepository.getFollowers(userId, limit, offset)

    override suspend fun getFollowing(userId: String, limit: Int, offset: Int) =
        followerRepository.getFollowing(userId, limit, offset)

    override suspend fun followUser(followerId: String, followedId: String): Boolean {
        if (followerId == followedId) throw IllegalArgumentException("No puedes seguirte a ti mismo")
        return followerRepository.followUser(followerId, followedId)
    }

    override suspend fun unfollowUser(followerId: String, followedId: String) =
        followerRepository.unfollowUser(followerId, followedId)

    override suspend fun isFollowing(userId: String, targetId: String) =
        followerRepository.isFollowing(userId, targetId)
}
