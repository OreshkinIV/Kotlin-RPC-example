package ktor.backend

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import ktor.backend.ktor.backend.plugins.db.configureDatabases
import ktor.backend.ktor.backend.plugins.routing.configureRouting
import ktor.backend.ktor.backend.plugins.serialization.installSerialization
import ktor.backend.plugins.auth.installAuth
import ktor.backend.plugins.krpc.installKRPC
import ktor.backend.plugins.statuspages.installStatusPages
import org.example.krpc.DEFAULT_PORT
import org.example.krpc.TLS_PORT
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.security.KeyStore

fun main() {
    embeddedServer(
        factory = Netty,
        environment = applicationEnvironment {
            log = LoggerFactory.getLogger("ktor.application")
        },
        configure = {
            envConfig()
        },
        module = Application::module
    ).start(wait = true)
}

private fun ApplicationEngine.Configuration.envConfig() {
    /** http */
    connector {
        port = DEFAULT_PORT
    }

    /** ssl
     * ВЕСЬ код ниже можно закомментить, если деплоить в railway так как он сам обрабатывает
     * tls соединение */
    val keyStoreFile = File("keystore.p12")

    /** при деплое и упаковке в jar доступ к хранилищу из ресурсов */
//    val keyStoreFile = Files.createTempFile("keystore", ".p12").toFile()
//    this::class.java.classLoader.getResourceAsStream("keystore.p12")?.use { input ->
//        keyStoreFile.outputStream().use { output ->
//            input.copyTo(output)
//        }
//    } ?: throw IllegalArgumentException("keystore.p12 не найден")

    val keystore: KeyStore = KeyStore.getInstance(
        keyStoreFile, "password".toCharArray()
    )
//
    sslConnector(
        keyStore = keystore,
        keyAlias = "cert",
        keyStorePassword = { "password".toCharArray() },
        privateKeyPassword = { "password".toCharArray() }) {
        port = TLS_PORT
        keyStorePath = keyStoreFile
    }
}

fun Application.module() {
    val config = ConfigFactory.load()
    installSerialization()
    installStatusPages()
    installAuth(config)
    installKRPC()
    configureDatabases(config)
    configureRouting(config)
}
