package org.example.com.mockclub.data.database.tables

import org.jetbrains.exposed.sql.Table

object PostTags : Table("post_tags") {
    val postUuid = uuid("post_uuid") references Posts.uuid
    val tagName = varchar("tag_name", 100) references Tags.name

    override val primaryKey = PrimaryKey(postUuid, tagName)
}