package org.example.com.mockclub.presentation.route

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.com.mockclub.domain.service.SearchService
import org.koin.ktor.ext.inject

fun Route.searchRoutes() {
    val searchService by inject<SearchService>()

    route("/search") {

        get("/posts/team") {
            val currentUserId = call.request.queryParameters["currentUserId"]
            val query = call.request.queryParameters["q"] ?: ""
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            if (currentUserId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing currentUserId")
                return@get
            }

            val results = searchService.searchPostsByTeam(currentUserId, query, limit, offset)
            call.respond(results)
        }

        get("/posts/brand") {
            val currentUserId = call.request.queryParameters["currentUserId"]
            val query = call.request.queryParameters["q"] ?: ""
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            if (currentUserId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing currentUserId")
                return@get
            }

            val results = searchService.searchPostsByBrand(currentUserId, query, limit, offset)
            call.respond(results)
        }

        get("/posts/tag") {
            val currentUserId = call.request.queryParameters["currentUserId"]
            val query = call.request.queryParameters["q"] ?: ""
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            if (currentUserId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing currentUserId")
                return@get
            }

            val results = searchService.searchPostsByTag(currentUserId, query, limit, offset)
            call.respond(results)
        }
    }
}
