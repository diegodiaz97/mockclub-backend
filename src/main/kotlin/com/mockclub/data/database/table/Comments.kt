package org.example.com.mockclub.data.database.tables

import com.mockclub.data.database.table.Users
import org.jetbrains.exposed.sql.Table

object Comments : Table("comments") {
    val uuid = uuid("uuid")
    val postUuid = uuid("post_uuid") references Posts.uuid
    val userUuid = varchar("user_uuid", 128) references Users.id
    val text = varchar("text", 500)
    val createdAt = long("created_at")
    val parentCommentUuid = uuid("parent_comment_uuid").nullable() // para replies

    override val primaryKey = PrimaryKey(uuid)
}
