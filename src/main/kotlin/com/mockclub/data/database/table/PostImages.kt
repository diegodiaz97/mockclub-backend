package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object PostImages : Table("post_images") {
    val id = integer("id").autoIncrement()
    val postId = varchar("post_id", 128) references Posts.id
    val imageUrl = varchar("image_url", 500)

    override val primaryKey = PrimaryKey(id)
}