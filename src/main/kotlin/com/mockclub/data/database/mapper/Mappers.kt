package com.mockclub.data.database.mapper

import com.mockclub.data.database.table.Comments
import com.mockclub.data.database.table.Followers
import com.mockclub.data.database.table.Likes
import com.mockclub.data.database.table.PostImages
import com.mockclub.data.database.table.PostTags
import com.mockclub.data.database.table.Posts
import com.mockclub.data.database.table.Tags
import com.mockclub.data.database.table.Users
import com.mockclub.domain.model.Comment
import com.mockclub.domain.model.Follower
import com.mockclub.domain.model.Like
import com.mockclub.domain.model.Post
import com.mockclub.domain.model.PostImage
import com.mockclub.domain.model.PostTag
import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.Tag
import com.mockclub.domain.model.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser(): User = User(
    id = this[Users.id],
    name = this[Users.name],
    lastName = this[Users.lastname],
    username = this[Users.username],
    description = this[Users.description],
    email = this[Users.email],
    country = this[Users.country],
    creationDate = this[Users.creationDate],
    userType = this[Users.userType],
    profileImage = ProfileImage(
        image = this[Users.profileImageUrl],
        background = this[Users.profileBackground],
        initials = this[Users.initials]
    )
)

fun ResultRow.toFollower(): Follower = Follower(
    followerId = this[Followers.followerId],
    followedId = this[Followers.followedId],
    createdAt = this[Followers.createdAt]
)

fun ResultRow.toPost(): Post = Post(
    id = this[Posts.id].toString(),
    userId = this[Posts.userId],
    createdAt = this[Posts.createdAt],
    text = this[Posts.text],
    brand = this[Posts.brand],
    team = this[Posts.team],
    ratio = this[Posts.ratio]
)

fun ResultRow.toPostImage(): PostImage = PostImage(
    id = this[PostImages.id],
    postId = this[PostImages.postId].toString(),
    imageUrl = this[PostImages.imageUrl]
)

fun ResultRow.toComment(): Comment = Comment(
    id = this[Comments.id].toString(),
    postId = this[Comments.postId].toString(),
    userId = this[Comments.userId],
    text = this[Comments.text],
    createdAt = this[Comments.createdAt],
    parentCommentId = this[Comments.parentCommentId]?.toString()
)

fun ResultRow.toLike(): Like = Like(
    userId = this[Likes.userId],
    postId = this[Likes.postId].toString()
)

fun ResultRow.toTag(): Tag = Tag(
    name = this[Tags.name],
    usageCount = this[Tags.usageCount]
)

fun ResultRow.toPostTag(): PostTag = PostTag(
    postId = this[PostTags.postId].toString(),
    tagName = this[PostTags.tagName]
)

/*fun ResultRow.toChallenge(): Challenge = Challenge(
    uuid = this[Challenges.uuid],
    createdAt = this[Challenges.createdAt],
    name = this[Challenges.name],
    description = this[Challenges.description],
    endTime = this[Challenges.endTime],
    maxImages = this[Challenges.maxImages]
)

fun ResultRow.toChallengeParticipation(): ChallengeParticipation = ChallengeParticipation(
    uuid = this[ChallengeParticipations.uuid],
    createdAt = this[ChallengeParticipations.createdAt],
    challengeUuid = this[ChallengeParticipations.challengeUuid],
    postUuid = this[ChallengeParticipations.postUuid]
)

fun ResultRow.toChallengeVote(): ChallengeVote = ChallengeVote(
    uuid = this[ChallengeVotes.uuid],
    createdAt = this[ChallengeVotes.createdAt],
    challengeUuid = this[ChallengeVotes.challengeUuid],
    postUuid = this[ChallengeVotes.postUuid],
    voterUserUuid = this[ChallengeVotes.voterUserUuid]
)*/
