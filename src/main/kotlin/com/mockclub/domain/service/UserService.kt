package org.example.com.mockclub.domain.service

import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User

interface UserService {
    suspend fun getUserById(id: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun createUser(user: User): Boolean
    suspend fun updateUser(user: User): Boolean
    suspend fun updateProfileImage(uid: String, image: ProfileImage): Boolean
    suspend fun deleteUser(id: String): Boolean
    suspend fun searchUsers(query: String, limit: Int, offset: Int): List<User>
}
