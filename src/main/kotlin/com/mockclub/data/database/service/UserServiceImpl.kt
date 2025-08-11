package org.example.com.mockclub.data.database.service

import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User
import org.example.com.mockclub.domain.repository.UserRepository
import org.example.com.mockclub.domain.service.UserService
import org.example.com.mockclub.utils.SimpleCache

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    val searchCache = SimpleCache<List<User>>(ttlMillis = 5 * 60 * 1000) // cache 5 minutos

    override suspend fun getUserById(uuid: String): User? {
        return userRepository.getUserById(uuid)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userRepository.getUserByUsername(username)
    }

    override suspend fun createUser(user: User): Boolean {
        if (user.username.isBlank() || user.email.isBlank()) {
            throw IllegalArgumentException("Username y email no pueden estar vacíos")
        }
        return userRepository.createUser(user)
    }

    override suspend fun updateUser(user: User): Boolean {
        val existing = userRepository.getUserById(user.id)
            ?: throw NoSuchElementException("Usuario no encontrado")
        return userRepository.updateUser(user)
    }

    override suspend fun updateProfileImage(uid: String, image: ProfileImage): Boolean {
        val existing = userRepository.getUserById(uid)
            ?: throw NoSuchElementException("Usuario no encontrado")
        return userRepository.updateProfileImage(uid, image)
    }

    override suspend fun deleteUser(uuid: String): Boolean {
        return userRepository.deleteUser(uuid)
    }

    override suspend fun searchUsers(query: String, limit: Int, offset: Int): List<User> {
        val normalizedQuery = query.trim().lowercase()
        val cacheKey = "$normalizedQuery|$limit|$offset"
        val cachedResult = searchCache.get(cacheKey)
        if (cachedResult != null) {
            println("Cache hit for $cacheKey")
            return cachedResult
        }

        println("Cache miss for $cacheKey, querying DB...")
        val users = userRepository.searchUsers(query, limit, offset) // tu función de búsqueda real
        searchCache.put(cacheKey, users)
        return users
    }
}
