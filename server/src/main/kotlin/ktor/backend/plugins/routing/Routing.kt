package ktor.backend.ktor.backend.plugins.routing

import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.*
import io.ktor.util.collections.ConcurrentSet
import kotlinx.rpc.krpc.ktor.server.rpc
import ktor.backend.ktor.backend.services.auth.AuthRpcServiceImpl
import ktor.backend.ktor.backend.services.auth.AuthRestServiceImpl
import ktor.backend.ktor.backend.services.user.UserRpcServiceImpl
import ktor.backend.plugins.auth.toJwtPayload
import org.example.krpc.services.AuthRpcService
import org.example.krpc.services.UserRpcService

fun Application.configureRouting(config: Config) {
    routing {
        /** http rest request */
        post("/register") {
            val registrationService = AuthRestServiceImpl(call, config)
            registrationService.registerNewUser()
        }

        /** krpc request */
        rpc("/auth") {
            registerService<AuthRpcService> { ctx -> AuthRpcServiceImpl(ctx, config) }
        }

        val allMessages = ConcurrentSet<String>()
        val receivedMessages = ConcurrentSet<String>()

        authenticate("auth-jwt") {
            rpc("/user") {
                val jwtPayload = call.principal<JWTPrincipal>().toJwtPayload()

                registerService<UserRpcService> { ctx -> UserRpcServiceImpl(ctx, jwtPayload, allMessages, receivedMessages) }
            }
        }
    }
}