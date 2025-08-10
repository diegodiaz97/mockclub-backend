package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = varchar("id", 128)
    val creationDate = long("creation_date")
    val country = varchar("country", 100).nullable()
    val name = varchar("name", 100).nullable()
    val lastname = varchar("lastname", 100).nullable()
    val username = varchar("username", 50).uniqueIndex()
    val description = varchar("description", 300).nullable()
    val email = varchar("email", 255).uniqueIndex()
    val profileImageUrl = varchar("image", 500).nullable()
    val profileBackground = varchar("background", 12).nullable() // "#FFFFFF"
    val initials = varchar("initials", 2).nullable()
    val userType = varchar("user_type", 20).default("basic")

    override val primaryKey = PrimaryKey(id)
}
