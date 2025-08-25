package com.mockclub.data.database

import com.mockclub.data.database.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    private const val MAX_RETRIES = 5
    private const val RETRY_DELAY_MS = 1000L // 1 segundo

    fun init() {
        var attempt = 0
        while (true) {
            try {
                val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://db:5432/mockclub"
                val dbUser = System.getenv("DB_USER") ?: "postgres"
                val dbPassword = System.getenv("DB_PASSWORD") ?: "postgres"

                println("📦 Connecting to DB at: $dbUrl with user: $dbUser (attempt ${attempt + 1})")

                Database.connect(
                    url = dbUrl,
                    driver = "org.postgresql.Driver",
                    user = dbUser,
                    password = dbPassword
                )

                transaction {
                    SchemaUtils.create(
                        Users,
                        Followers,
                        Posts,
                        PostImages,
                        PostTags,
                        Tags,
                        Likes,
                        Comments,
                        CommentLikes
                    )
                }

                println("✅ Database initialized successfully")
                break // conexión ok, salimos del loop

            } catch (e: Exception) {
                attempt++
                println("⚠️ Failed to connect to DB (attempt $attempt/$MAX_RETRIES): ${e.message}")
                if (attempt >= MAX_RETRIES) {
                    println("❌ Could not connect to DB after $MAX_RETRIES attempts, aborting")
                    throw e
                }
                Thread.sleep(RETRY_DELAY_MS)
            }
        }
    }
}
