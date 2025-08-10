plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" // 👈 AGREGÁ ESTA LÍNEA
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"  // <-- Agregado
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.mockclub.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/maven") }
}

val ktorVersion = "3.2.2"

dependencies {
    implementation("io.ktor:ktor-server-core:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("io.ktor:ktor-server-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("io.ktor:ktor-server-call-logging:${ktorVersion}")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.50.1")

    implementation("org.postgresql:postgresql:42.7.3") // Driver

    // Koin para Ktor
    implementation("io.insert-koin:koin-ktor:4.1.0")
    implementation("io.insert-koin:koin-logger-slf4j:4.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("com.google.firebase:firebase-admin:9.2.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
