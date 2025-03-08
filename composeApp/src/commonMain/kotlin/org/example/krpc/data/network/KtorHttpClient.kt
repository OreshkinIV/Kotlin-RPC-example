package org.example.krpc.data.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import org.example.krpc.INVALID_REFRESH_TOKEN_CODE
import org.example.krpc.RAILWAY_DOMAIN
import org.example.krpc.TLS_PORT
import org.example.krpc.data.preferences.getRefreshToken
import org.example.krpc.data.preferences.getToken
import org.example.krpc.di.inject
import org.example.krpc.domain.usecases.auth.rpc.RefreshTokenUseCase
import co.touchlab.kermit.Logger as Kermit

private const val TIMEOUT_MILLIS = 20_000L

/** ktor client with krpc */
internal fun ktorClient(): HttpClient = HttpClient {
    /** krpc and WebSocket */
    installKrpc {
        waitForServices = true
    }
    /** content type, Serializing/deserializing */
    install(ContentNegotiation) {
        json(
            json = Json {
                /** ignore unknown properties without exception */
                ignoreUnknownKeys = true
                /** formatted and optimized for human readability */
                prettyPrint = true
            }
        )
    }
    configureForPlatform()
    defaultRequest {
        url {
            /** railway */
//            host = RAILWAY_DOMAIN
//            port = 0

            /** local tls */
            port = TLS_PORT
            host = getLocalHost()

            /** local http */
//            port = SERVER_PORT
//            host = getLocalHost()
        }
    }
    /** timeout */
    install(HttpTimeout) {
        connectTimeoutMillis = TIMEOUT_MILLIS
        requestTimeoutMillis = TIMEOUT_MILLIS
//        socketTimeoutMillis = TIMEOUT_MILLIS
    }
    /** http logging */
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                /** multiplatform logs */
                Kermit.i { message }
            }
        }
    }
}.also {
    /** обработка 401 кода */
    it.plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)
        if (originalCall.response.status == HttpStatusCode.Unauthorized) {
            val refreshTokenUseCase = inject<RefreshTokenUseCase>()
            val dataStore = inject<DataStore<Preferences>>()

            /** запрашиваем новые токены */
            val refreshToken = dataStore.getRefreshToken()
            val rpcResponse = refreshTokenUseCase.invoke(refreshToken)

            if (rpcResponse.error?.code == INVALID_REFRESH_TOKEN_CODE) {
                /** какая-нибудь логика разлогина */
                throw InvalidRefreshTokenException()
            } else {
                /** повторяем запрос с новым токеном */
                val authToken = dataStore.getToken()
                request.headers.remove(HttpHeaders.Authorization)
                request.header(HttpHeaders.Authorization, "Bearer $authToken")
                execute(request)
            }
        } else {
            originalCall
        }
    }
}

class InvalidRefreshTokenException() : Throwable()

internal expect fun HttpClientConfig<*>.configureForPlatform()
