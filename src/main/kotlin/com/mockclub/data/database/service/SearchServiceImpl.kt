package org.example.com.mockclub.data.database.service

import org.example.com.mockclub.domain.model.composite.PostWithExtras
import org.example.com.mockclub.domain.repository.SearchRepository
import org.example.com.mockclub.domain.service.SearchService

class SearchServiceImpl(
    private val searchRepository: SearchRepository
) : SearchService {

    override suspend fun searchPostsByTeam(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> {
        return searchRepository.searchPostsByTeam(currentUserId, query, limit, offset)
    }

    override suspend fun searchPostsByBrand(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> {
        return searchRepository.searchPostsByBrand(currentUserId, query, limit, offset)
    }

    override suspend fun searchPostsByTag(
        currentUserId: String,
        query: String,
        limit: Int,
        offset: Int
    ): List<PostWithExtras> {
        return searchRepository.searchPostsByTag(currentUserId, query, limit, offset)
    }
}
