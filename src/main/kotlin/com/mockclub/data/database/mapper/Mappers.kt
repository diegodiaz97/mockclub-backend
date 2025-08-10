package org.example.com.mockclub.data.database.mappers

import com.mockclub.data.database.table.Users
import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User
import org.example.com.mockclub.data.database.models.*
import org.example.com.mockclub.data.database.tables.*
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

fun ResultRow.toPost(): Post = Post(
    uuid = this[Posts.uuid].toString(),
    userUuid = this[Posts.userUuid],
    createdAt = this[Posts.createdAt],
    text = this[Posts.text],
    brand = this[Posts.brand],
    team = this[Posts.team],
    ratio = this[Posts.ratio]
)

fun ResultRow.toPostImage(): PostImage = PostImage(
    id = this[PostImages.id],
    postUuid = this[PostImages.postUuid].toString(),
    imageUrl = this[PostImages.imageUrl]
)

fun ResultRow.toComment(): Comment = Comment(
    uuid = this[Comments.uuid].toString(),
    postUuid = this[Comments.postUuid].toString(),
    userUuid = this[Comments.userUuid],
    text = this[Comments.text],
    createdAt = this[Comments.createdAt],
    parentCommentUuid = this[Comments.parentCommentUuid]?.toString()
)

fun ResultRow.toLike(): Like = Like(
    userUuid = this[Likes.userUuid],
    postUuid = this[Likes.postUuid].toString()
)

fun ResultRow.toTag(): Tag = Tag(
    name = this[Tags.name],
    usageCount = this[Tags.usageCount]
)

fun ResultRow.toPostTag(): PostTag = PostTag(
    postUuid = this[PostTags.postUuid].toString(),
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
