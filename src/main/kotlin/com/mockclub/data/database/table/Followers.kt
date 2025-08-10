package org.example.com.mockclub.data.database.tables

import com.mockclub.data.database.table.Users
import org.jetbrains.exposed.sql.Table

object Followers : Table("followers") {
    val follower = varchar("follower", 128) references Users.id
    val followed = varchar("followed", 128) references Users.id

    override val primaryKey = PrimaryKey(follower, followed)
}
