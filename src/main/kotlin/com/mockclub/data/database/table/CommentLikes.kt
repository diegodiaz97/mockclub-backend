package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object CommentLikes : Table("comment_likes") {
    val userId = varchar("user_id", 128) references Users.id
    val commentId = varchar("comment_id", 128) references Comments.id

    override val primaryKey = PrimaryKey(userId, commentId)
}