package org.example.com.mockclub.domain.repository

import org.example.com.mockclub.domain.model.composite.PostWithExtras

interface SearchRepository {
    suspend fun searchPostsByTeam(currentUserId: String, query: String, limit: Int, offset: Int): List<PostWithExtras>
    suspend fun searchPostsByBrand(currentUserId: String, query: String, limit: Int, offset: Int): List<PostWithExtras>
    suspend fun searchPostsByTag(currentUserId: String, query: String, limit: Int, offset: Int): List<PostWithExtras>
}
