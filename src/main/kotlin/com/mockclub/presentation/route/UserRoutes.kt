package org.example.com.mockclub.presentation.route

import com.mockclub.domain.model.ProfileImage
import com.mockclub.domain.model.User
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.com.mockclub.domain.service.UserService
import org.example.com.mockclub.utils.FirebaseUidKey
import org.example.com.mockclub.utils.firebaseAuthProtectedRoute
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userService by inject<UserService>()

    route("/users") {

        // 🔓 Rutas públicas

        // GET /users/username/{username}
        get("/username/{username}") {
            val username = call.parameters["username"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = userService.getUserByUsername(username)
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
            }
        }

        // 🔐 Rutas protegidas por Firebase Auth
        firebaseAuthProtectedRoute {
            // GET /users/{id}
            get("{id}") {
                val idParam = call.parameters["id"]
                if (idParam == null) {
                    return@get call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                }

                val user = userService.getUserById(idParam)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            }

            get("/search") {
                val query = call.request.queryParameters["q"] ?: ""
                if (query.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Query parameter 'q' is required")
                    return@get
                }
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 15
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                val users = userService.searchUsers(query, limit, offset)
                call.respond(users)
            }

            // POST /users
            post {
                val uid = call.attributes[FirebaseUidKey]
                val user = call.receive<User>().copy(id = uid)

                val success = userService.createUser(user)
                if (success) {
                    call.respond(HttpStatusCode.Created, "Usuario creado")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo crear el usuario")
                }
            }

            // PUT /users/{id}
            put("{id}") {
                val uid = call.attributes[FirebaseUidKey]
                val user = call.receive<User>().copy(id = uid)

                val success = userService.updateUser(user)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Usuario actualizado")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo actualizar el usuario")
                }
            }

            // PUT /users/{id}
            put("{id}/photo") {
                val uid = call.attributes[FirebaseUidKey]
                val profileImage = call.receive<ProfileImage>()
                val success = userService.updateProfileImage(uid, profileImage)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Imagen de perfil actualizada")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo actualizar la imagen de perfil")
                }
            }

            // DELETE /users/{id}
            delete("{id}") {
                val uid = call.attributes[FirebaseUidKey]

                val success = userService.deleteUser(uid)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Usuario eliminado")
                } else {
                    call.respond(HttpStatusCode.NotFound, "No se pudo eliminar el usuario")
                }
            }
        }
    }
}
