package ktor.backend.plugins.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import org.example.krpc.models.responses.JwtPayload

fun Application.installAuth(config: Config) {
    val secret = config.getConfig("jwt").getString("secret")
    val issuer = config.getConfig("jwt").getString("issuer")
    val audience = config.getConfig("jwt").getString("audience")
    val myRealm = config.getConfig("jwt").getString("realm")

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                /** доп проверки, например имя пользователя */
//                if (credential.payload.getClaim("your_claim").asString() != "") {
//                    JWTPrincipal(credential.payload)
//                } else {
//                    null
//                }
                JWTPrincipal(credential.payload)
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

fun JWTPrincipal?.toJwtPayload(): JwtPayload {
    return JwtPayload(
        expiresAt = this?.expiresAt?.time?.minus(System.currentTimeMillis()),
        audience = this?.audience,
        issuer = this?.payload?.issuer,
    )
}