package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object PostTags : Table("post_tags") {
    val postId = varchar("post_id", 128) references Posts.id
    val tagName = varchar("tag_name", 100) references Tags.name

    override val primaryKey = PrimaryKey(postId, tagName)
}