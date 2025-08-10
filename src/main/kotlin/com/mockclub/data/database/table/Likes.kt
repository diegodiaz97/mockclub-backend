package org.example.com.mockclub.data.database.tables

import com.mockclub.data.database.table.Users
import org.jetbrains.exposed.sql.Table

object Likes : Table("likes") {
    val userUuid = varchar("user_uuid", 128) references Users.id
    val postUuid = uuid("post_uuid") references Posts.uuid

    override val primaryKey = PrimaryKey(userUuid, postUuid)
}
