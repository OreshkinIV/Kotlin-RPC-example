package ktor.backend.ktor.backend.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import org.example.krpc.models.requests.AuthBody
import ktor.backend.ktor.backend.db.postgresql.tables.users.User
import ktor.backend.ktor.backend.db.postgresql.tables.users.Users
import org.example.krpc.INVALID_REFRESH_TOKEN_CODE
import org.example.krpc.models.responses.ErrorResponse
import org.example.krpc.models.responses.RpcResponse
import org.example.krpc.models.responses.UserResponse
import org.example.krpc.services.AuthRpcService
import java.util.*
import kotlin.coroutines.CoroutineContext

/** krpc example */
class AuthRpcServiceImpl(
    override val coroutineContext: CoroutineContext,
    config: Config,
) : AuthRpcService {

    private val secret = config.getConfig("jwt").getString("secret")
    private val issuer = config.getConfig("jwt").getString("issuer")
    private val audience = config.getConfig("jwt").getString("audience")

    private val expiresAuth =  config.getConfig("jwt").getString("expiresAuth").toLong()
    private val expireRefresh = config.getConfig("jwt").getString("expiresRefresh").toLong()

    private fun verifyToken(token: String) = try {
        val decodedJwt = JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
            .verify(token)
    } catch (e: Throwable) {
        null
    }

    private fun createToken(expires: Long) = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expires))
        .sign(Algorithm.HMAC256(secret))

    override suspend fun registerNewUser(body: AuthBody): RpcResponse<UserResponse?> {
        val user = Users.getUser(body.login)
        return if (user != null) {
            RpcResponse(data = null, error = ErrorResponse("User already exists"))
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
                RpcResponse(data = null, error = ErrorResponse("Can't create user ${e.message}"))
            }

            return RpcResponse(data = UserResponse(authToken, refreshToken))
        }
    }

    override suspend fun login(body: AuthBody): RpcResponse<UserResponse?> {
        val userDTO = Users.getUser(body.login)
        return if (userDTO != null) {
            val authToken = createToken(expiresAuth)
            val refreshToken = createToken(expireRefresh)

            RpcResponse(data = UserResponse(authToken, refreshToken))
        } else {
            RpcResponse(data = null, error = ErrorResponse("User not exists"))
        }
    }

    override suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?> {
        return if (verifyToken(refreshToken) != null) {
            val auth = createToken(expiresAuth)
            val refresh = createToken(expireRefresh)

            RpcResponse(UserResponse(auth, refresh))
        } else {
            RpcResponse(data = null, error = ErrorResponse("Invalid refresh token", INVALID_REFRESH_TOKEN_CODE))
        }
    }
}