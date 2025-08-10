package org.example.com.mockclub.data.database.tables

import org.jetbrains.exposed.sql.Table

object PostImages : Table("post_images") {
    val id = integer("id").autoIncrement()
    val postUuid = uuid("post_uuid") references Posts.uuid
    val imageUrl = varchar("image_url", 500)

    override val primaryKey = PrimaryKey(id)
}