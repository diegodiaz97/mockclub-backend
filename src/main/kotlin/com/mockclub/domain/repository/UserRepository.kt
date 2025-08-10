package org.example.com.mockclub.domain.repository

import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User

interface UserRepository {
    suspend fun getUserById(uuid: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
    suspend fun updateProfileImage(uid: String, image: ProfileImage): Boolean
    suspend fun deleteUser(uuid: String): Boolean
}
