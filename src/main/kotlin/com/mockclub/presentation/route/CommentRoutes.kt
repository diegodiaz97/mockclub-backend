package org.example.com.mockclub.presentation.route

import com.mockclub.domain.model.Comment
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.com.mockclub.domain.model.composite.CommentWithExtras
import org.example.com.mockclub.domain.request.CreateCommentRequest
import org.example.com.mockclub.domain.request.EditCommentRequest
import org.example.com.mockclub.domain.service.CommentService
import org.example.com.mockclub.domain.service.UserService
import org.example.com.mockclub.utils.FirebaseUidKey
import org.example.com.mockclub.utils.firebaseAuthProtectedRoute
import org.koin.ktor.ext.inject
import java.util.*

fun Route.commentRoutes() {
    val commentService by inject<CommentService>()
    val userService by inject<UserService>()

    route("/comments") {
        firebaseAuthProtectedRoute {

            // 1. Obtener comentarios de un post (paginado)
            get("/post/{postId}") {
                val userId = call.attributes[FirebaseUidKey]

                val postId = call.parameters["postId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val cursorCreatedAt = call.request.queryParameters["cursorCreatedAt"]?.toLongOrNull()

                val comments = commentService.getCommentsForPost(userId, postId, limit, cursorCreatedAt)
                call.respond(comments)
            }

            // Obtener replies de un comentario (paginado)
            get("/replies/{commentId}") {
                val userId = call.attributes[FirebaseUidKey]

                val commentId = call.parameters["commentId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5
                val cursorCreatedAt = call.request.queryParameters["cursorCreatedAt"]?.toLongOrNull()

                val replies = commentService.getRepliesForComment(
                    currentUserId = userId,
                    parentCommentId = commentId,
                    limit = limit,
                    cursorCreatedAt = cursorCreatedAt
                )

                if (replies.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(replies)
                }
            }

            // 2. Crear comentario o reply
            post("/create") {
                val userId = call.attributes[FirebaseUidKey]
                val request = call.receive<CreateCommentRequest>()

                val comment = Comment(
                    id = UUID.randomUUID().toString(),
                    postId = request.postId,
                    userId = userId,
                    text = request.text,
                    createdAt = System.currentTimeMillis(),
                    parentCommentId = request.parentCommentId
                )

                val success = commentService.createComment(comment)
                if (success) {
                    // Obtenemos el user y devolvemos el comment completo
                    val user = userService.getUserById(userId)
                    if (user == null) {
                        call.respond(HttpStatusCode.InternalServerError, "Usuario no encontrado")
                        return@post
                    }
                    val commentWithExtras = CommentWithExtras(
                        comment = comment,
                        user = user,
                        likeCount = 0,
                        likedByCurrentUser = false,
                        replyCount = 0
                    )
                    call.respond(HttpStatusCode.Created, commentWithExtras)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            // 3. Dar like a un comentario
            post("/{id}/like") {
                val commentId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                commentService.likeComment(commentId, userId)
                call.respond(HttpStatusCode.OK)
            }

            // 4. Quitar like de un comentario
            delete("/{id}/like") {
                val commentId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                commentService.unlikeComment(commentId, userId)
                call.respond(HttpStatusCode.OK)
            }

            // 5. Obtener lista de usuarios que dieron like
            get("/{id}/likes") {
                val commentId = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                val likes = commentService.getCommentLikes(commentId, limit, offset)
                call.respond(likes)
            }

            // 6. Editar comentario (opcional)
            put("/{id}") {
                val commentId = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]
                val request = call.receive<EditCommentRequest>()

                val updated = commentService.editComment(commentId, userId, request.text)
                if (updated) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.Forbidden)
            }

            // 7. Borrar comentario o reply
            delete("/{id}") {
                val commentId = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val userId = call.attributes[FirebaseUidKey]

                val deleted = commentService.deleteComment(commentId, userId)
                if (deleted) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}
