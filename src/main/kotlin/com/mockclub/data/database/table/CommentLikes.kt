package org.example.com.mockclub.data.database.tables

import com.mockclub.data.database.table.Users
import org.jetbrains.exposed.sql.Table

object CommentLikes : Table("comment_likes") {
    val userUuid = varchar("user_uuid", 128) references Users.id
    val commentUuid = uuid("comment_uuid") references Comments.uuid

    override val primaryKey = PrimaryKey(userUuid, commentUuid)
}