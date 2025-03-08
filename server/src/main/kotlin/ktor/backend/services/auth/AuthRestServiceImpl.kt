package ktor.backend.ktor.backend.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ktor.backend.ktor.backend.db.postgresql.tables.users.Users
import org.example.krpc.models.requests.AuthBody
import ktor.backend.ktor.backend.db.postgresql.tables.users.User
import org.example.krpc.models.responses.UserResponse
import java.util.*

/** rest api example */
class AuthRestServiceImpl(
    private val call: ApplicationCall,
    config: Config
) {
    private val secret = config.getConfig("jwt").getString("secret")
    private val issuer = config.getConfig("jwt").getString("issuer")
    private val audience = config.getConfig("jwt").getString("audience")

    private val expiresAuth =  config.getConfig("jwt").getString("expiresAuth").toLong()
    private val expireRefresh = config.getConfig("jwt").getString("expiresRefresh").toLong()

    private fun createToken(expires: Long) = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expires))
        .sign(Algorithm.HMAC256(secret))

    suspend fun registerNewUser() {
        val body = call.receive<AuthBody>()

        val user = Users.getUser(body.login)
        if (user != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val authToken = createToken(expiresAuth)
            val refreshToken = createToken(expireRefresh)

            try {
                Users.insert(
                    User(
                        login = body.login,
                        password = body.password,
                    )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Can't create user ${e.message}")
            }

            call.respond(UserResponse(authToken = authToken, refreshToken = refreshToken))
        }
    }
}