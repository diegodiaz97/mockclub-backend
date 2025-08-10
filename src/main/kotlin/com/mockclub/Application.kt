package com.mockclub

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.mockclub.data.database.DatabaseFactory
import com.mockclub.presentation.route.healthRoute
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.example.com.mockclub.di.appModule
import org.example.com.mockclub.presentation.route.userRoutes
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import java.io.FileInputStream

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val serviceAccount = FileInputStream("serviceAccountKey.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
    install(CallLogging) {
        level = Level.INFO  // También podés usar DEBUG si querés más detalle
    }
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false // no obliga a mandar las claves con null
                coerceInputValues = true
            }
        )
    }
    install(Koin) {
        slf4jLogger() // opcional
        modules(appModule)
    }

    DatabaseFactory.init()  // Inicializo la base

    routing {
        healthRoute()
        userRoutes()
        // otras rutas...
    }
}
