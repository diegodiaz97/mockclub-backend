package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Posts : Table("posts") {
    val id = varchar("id", 128)
    val userId = varchar("user_id", 128) references Users.id
    val createdAt = long("created_at")
    val text = varchar("text", 200)
    val team = varchar("team", 100)
    val brand = varchar("brand", 100)
    val teamLogo = varchar("team_logo", 500)
    val brandLogo = varchar("brand_logo", 500)
    val designerLogo = varchar("designer_logo", 500).nullable()
    val ratio = float("ratio")

    override val primaryKey = PrimaryKey(id)
}
