package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Likes : Table("likes") {
    val userId = varchar("user_id", 128) references Users.id
    val postId = varchar("post_id", 128) references Posts.id

    override val primaryKey = PrimaryKey(userId, postId)
}
