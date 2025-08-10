package com.mockclub.presentation.route

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.healthRoute() {
    val logger = LoggerFactory.getLogger("HealthCheck")

    get("/health") {
        logger.info("Received /health request")
        call.respondText("Healthy")
        logger.info("Responded with 'Healthy'")
    }
}
