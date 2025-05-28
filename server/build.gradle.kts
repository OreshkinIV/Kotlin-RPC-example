import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlinx.rpc.plugin)
    application
}

group = "org.example.krpc"
version = "1.0.0"

application {
    mainClass.set("ktor.backend.ApplicationKt")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("ktor-backend")
        imageTag.set("0.0.1")

        portMappings.set(
            listOf(
                DockerPortMapping(outsideDocker = 80, insideDocker = 8080, DockerPortMappingProtocol.TCP)
            )
        )
    }

    fatJar {
        archiveFileName.set("server-all.jar")
    }
}

val osName = System.getProperty("os.name").lowercase()
val tcnative_classifier = when {
    osName.contains("win") -> "windows-x86_64"
    osName.contains("linux") -> "linux-x86_64"
    osName.contains("mac") -> "osx-x86_64"
    else -> null
}

dependencies {
    /** shared module */
    implementation(projects.shared)

    /** ktor server and engine */
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.tls)
    implementation(libs.ktor.status.pages)

    /** ktor content and serialization */
    implementation(libs.ktor.server.content)
    implementation(libs.ktor.serialization.kotlinx.json)

    /** krpc server */
    implementation(libs.krpc.server)
    implementation(libs.krpc.ktor.server)

    /** krpc serialization */
    implementation(libs.krpc.serialization)

    /** config from .yaml files, example - resources/application.yaml */
    implementation(libs.ktor.server.config.yaml)

    /** auth */
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    /** storage */
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgress)

    /** logs */
    implementation(libs.logback)

//    /** http 2 */
//    if (tcnative_classifier != null) {
//        implementation("io.netty:netty-tcnative-boringssl-static:2.0.67.Final:$tcnative_classifier")
//    } else {
//        implementation(libs.netty.http2)
//    }
}
