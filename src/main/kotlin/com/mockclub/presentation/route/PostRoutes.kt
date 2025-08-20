package org.example.com.mockclub.presentation.route

import com.mockclub.domain.model.Post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.com.mockclub.domain.request.CreatePostRequest
import org.example.com.mockclub.domain.service.PostService
import org.example.com.mockclub.utils.FirebaseUidKey
import org.example.com.mockclub.utils.firebaseAuthProtectedRoute
import org.koin.ktor.ext.inject
import java.util.*

fun Route.postRoutes() {
    val postService by inject<PostService>()

    route("/posts") {
        firebaseAuthProtectedRoute {
            get("/feed") {
                val userId = call.attributes[FirebaseUidKey]
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val cursorCreatedAt = call.request.queryParameters["cursorCreatedAt"]?.toLongOrNull()

                val feed = postService.getFeed(userId, limit, cursorCreatedAt)
                call.respond(feed)
            }

            get("/feed/{userId}") {
                val currentUserId = call.attributes[FirebaseUidKey] // usuario logueado
                val userIdParam = call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val cursorCreatedAt = call.request.queryParameters["cursorCreatedAt"]?.toLongOrNull()

                val feed = postService.getFeedByUser(currentUserId, userIdParam, limit, cursorCreatedAt)
                call.respond(feed)
            }

            post("/create") {
                val userId = call.attributes[FirebaseUidKey]
                val request = call.receive<CreatePostRequest>()
                val post = Post(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    createdAt = System.currentTimeMillis(),
                    text = request.text,
                    brand = request.brand,
                    team = request.team,
                    teamLogo = request.teamLogo,
                    brandLogo = request.brandLogo,
                    designerLogo = request.designerLogo,
                    ratio = request.ratio
                )
                val success = postService.createPost(userId, post, request.images, request.tags)
                if (success) call.respond(HttpStatusCode.Created)
                else call.respond(HttpStatusCode.InternalServerError)
            }

            delete("/{id}") {
                val postId =
                    call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Post ID missing")

                try {
                    val deleted = postService.deletePost(postId)
                    if (deleted) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Post not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error deleting post")
                }
            }

            get("{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                postService.getPostById(id, userId)?.let {
                    call.respond(it)
                } ?: call.respond(HttpStatusCode.NotFound)
            }

            post("/{id}/like") {
                val postId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                postService.likePost(postId, userId)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}/like") {
                val postId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                postService.unlikePost(postId, userId)
                call.respond(HttpStatusCode.OK)
            }

            get("{id}/likes") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20  // default 20
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0  // default 0

                val likes = postService.getPostLikes(id, limit, offset)
                call.respond(likes)
            }

            get("/count/{userId}") {
                val userId = call.parameters["userId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Invalid userId"
                )

                call.respond(mapOf("count" to postService.getPostCountByUser(userId)))
            }
        }
    }
}
