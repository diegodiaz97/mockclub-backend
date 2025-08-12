package com.mockclub.data.database.table

import org.jetbrains.exposed.sql.Table

object Tags : Table("tags") {
    val name = varchar("name", 100)
    val usageCount = integer("usage_count").default(0)

    override val primaryKey = PrimaryKey(name)
}
