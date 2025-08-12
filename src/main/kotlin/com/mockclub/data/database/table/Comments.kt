package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Comments : Table("comments") {
    val id = varchar("id", 128)
    val postId = varchar("post_id", 128) references Posts.id
    val userId = varchar("user_id", 128) references Users.id
    val text = varchar("text", 500)
    val createdAt = long("created_at")
    val parentCommentId = varchar("parent_comment_id", 128).nullable() // para replies

    override val primaryKey = PrimaryKey(id)
}
