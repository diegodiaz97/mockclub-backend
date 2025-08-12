package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Followers : Table("followers") {
    val followerId = varchar("follower_id", 128).references(Users.id)
    val followedId = varchar("followed_id", 128).references(Users.id)
    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(followerId, followedId)
}
