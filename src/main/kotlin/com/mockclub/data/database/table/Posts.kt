package org.example.com.mockclub.data.database.tables

import com.mockclub.data.database.table.Users
import org.jetbrains.exposed.sql.Table

object Posts : Table("posts") {
    val uuid = uuid("uuid")
    val userUuid = varchar("user_uuid", 128) references Users.id
    val createdAt = long("created_at")
    val text = varchar("text", 200)
    val brand = varchar("brand", 100)
    val team = varchar("team", 100)
    val ratio = float("ratio")

    override val primaryKey = PrimaryKey(uuid)
}
