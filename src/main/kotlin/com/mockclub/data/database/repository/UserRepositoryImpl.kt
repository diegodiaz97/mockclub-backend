package org.example.com.mockclub.data.database.repository

import com.mockclub.data.database.table.Users
import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User
import org.example.com.mockclub.data.database.mappers.toUser
import org.example.com.mockclub.domain.repository.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserById(uuid: String): User? = newSuspendedTransaction {
        Users.selectAll().where { Users.id eq uuid }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun getUserByUsername(username: String): User? = newSuspendedTransaction {
        Users.selectAll().where { Users.username eq username }
            .map { it.toUser() }
            .singleOrNull()
    }

    override suspend fun createUser(user: User): Boolean = newSuspendedTransaction {
        Users.insert {
            it[this.id] = user.id
            it[name] = user.name
            it[lastname] = user.lastName
            it[username] = user.username
            it[description] = user.description
            it[email] = user.email
            it[country] = user.country
            it[creationDate] = user.creationDate
            it[userType] = user.userType
            it[profileImageUrl] = user.profileImage?.image
            it[profileBackground] = user.profileImage?.background
            it[initials] = user.profileImage?.initials
        }.insertedCount > 0
    }

    override suspend fun updateUser(user: User): Boolean = newSuspendedTransaction {
        Users.update({ Users.id eq user.id }) {
            it[name] = user.name
            it[lastname] = user.lastName
            it[description] = user.description
            it[country] = user.country
            it[initials] = user.profileImage?.initials
        } > 0
    }

    override suspend fun updateProfileImage(uid: String, image: ProfileImage): Boolean = newSuspendedTransaction {
        Users.update({ Users.id eq uid }) {
            it[profileImageUrl] = image.image
        } > 0
    }

    override suspend fun deleteUser(uuid: String): Boolean = newSuspendedTransaction {
        Users.deleteWhere { Users.id eq uuid } > 0
    }

    override suspend fun searchUsers(query: String, limit: Int, offset: Int): List<User> = newSuspendedTransaction {
        val terms = query.split(" ").filter { it.isNotBlank() }
        Users.selectAll().where {
            terms.map { term ->
                val pattern = "%${term.lowercase()}%"
                (Users.name.lowerCase() like pattern) or
                (Users.lastname.lowerCase() like pattern) or
                (Users.username.lowerCase() like pattern) or
                (Users.email.lowerCase() like pattern)
            }.reduce { acc, expr -> acc and expr }
        }.limit(limit, offset.toLong())
            .map { it.toUser() }
    }
}
