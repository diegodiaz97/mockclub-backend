package org.example.com.mockclub.utils

import java.util.concurrent.ConcurrentHashMap

data class CacheEntry<T>(
    val data: T,
    val timestamp: Long = System.currentTimeMillis()
)

class SimpleCache<T>(private val ttlMillis: Long) {
    private val cache = ConcurrentHashMap<String, CacheEntry<T>>()

    fun get(key: String): T? {
        val entry = cache[key] ?: return null
        // Si el cache expiró, borralo y retorna null
        if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
            cache.remove(key)
            return null
        }
        return entry.data
    }

    fun put(key: String, data: T) {
        cache[key] = CacheEntry(data)
    }
}
