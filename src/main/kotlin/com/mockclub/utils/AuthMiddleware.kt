package org.example.com.mockclub.utils

import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

val FirebaseUidKey = AttributeKey<String>("FirebaseUid")

fun Route.firebaseAuthProtectedRoute(build: Route.() -> Unit) {
    route("") {
        intercept(ApplicationCallPipeline.Plugins) {
            val authHeader = call.request.headers["Authorization"]
            val token = authHeader?.removePrefix("Bearer ")?.trim()

            if (token.isNullOrBlank()) {
                call.respond(HttpStatusCode.Unauthorized, "Token ausente")
                finish()
                return@intercept
            }

            try {
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val uid = decodedToken.uid
                call.attributes.put(FirebaseUidKey, uid)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido")
                finish()
            }
        }

        build()
    }
}
