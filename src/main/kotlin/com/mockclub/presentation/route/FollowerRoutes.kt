package org.example.com.mockclub.presentation.route

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.com.mockclub.domain.service.FollowerService
import org.example.com.mockclub.utils.FirebaseUidKey
import org.example.com.mockclub.utils.firebaseAuthProtectedRoute
import org.koin.ktor.ext.inject

fun Route.followerRoutes() {
    val followerService by inject<FollowerService>()

    route ("/users") {

        get("{id}/followers/count") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(mapOf("count" to followerService.getFollowersCount(id)))
        }

        get("{id}/following/count") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            call.respond(mapOf("count" to followerService.getFollowingCount(id)))
        }

        get("{id}/followers") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
            call.respond(followerService.getFollowers(id, limit, offset))
        }

        get("{id}/following") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
            call.respond(followerService.getFollowing(id, limit, offset))
        }

        firebaseAuthProtectedRoute {
            post("{id}/follow") {
                val uid = call.attributes[FirebaseUidKey]
                val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                if (followerService.followUser(uid, id)) {
                    call.respond(HttpStatusCode.OK, "Ahora sigues al usuario")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo seguir al usuario")
                }
            }

            delete("{id}/follow") {
                val uid = call.attributes[FirebaseUidKey]
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (followerService.unfollowUser(uid, id)) {
                    call.respond(HttpStatusCode.OK, "Has dejado de seguir al usuario")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo dejar de seguir")
                }
            }

            get("{id}/is-following") {
                val uid = call.attributes[FirebaseUidKey] // usuario logueado
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val isFollowing = followerService.isFollowing(uid, id)
                call.respond(mapOf("isFollowing" to isFollowing))
            }
        }
    }
}
