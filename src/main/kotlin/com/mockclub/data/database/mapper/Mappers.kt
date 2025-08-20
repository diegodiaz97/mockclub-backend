package com.mockclub.data.database.mapper

import com.mockclub.data.database.table.*
import com.mockclub.domain.model.*
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
    id = this[Posts.id],
    userId = this[Posts.userId],
    createdAt = this[Posts.createdAt],
    text = this[Posts.text],
    team = this[Posts.team],
    brand = this[Posts.brand],
    teamLogo = this[Posts.teamLogo],
    brandLogo = this[Posts.brandLogo],
    designerLogo = this[Posts.designerLogo],
    ratio = this[Posts.ratio]
)

fun ResultRow.toPostImage(): PostImage = PostImage(
    id = this[PostImages.id],
    postId = this[PostImages.postId],
    imageUrl = this[PostImages.imageUrl]
)

fun ResultRow.toComment(): Comment = Comment(
    id = this[Comments.id],
    postId = this[Comments.postId],
    userId = this[Comments.userId],
    text = this[Comments.text],
    createdAt = this[Comments.createdAt],
    parentCommentId = this[Comments.parentCommentId]
)

fun ResultRow.toLike(): Like = Like(
    userId = this[Likes.userId],
    postId = this[Likes.postId],
)

fun ResultRow.toTag(): Tag = Tag(
    name = this[Tags.name],
    usageCount = this[Tags.usageCount],
)

fun ResultRow.toPostTag(): PostTag = PostTag(
    postId = this[PostTags.postId],
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
