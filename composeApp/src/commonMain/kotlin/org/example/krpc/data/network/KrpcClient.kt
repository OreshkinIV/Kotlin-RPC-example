package org.example.krpc.data.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.RpcClient
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.krpc.streamScoped
import kotlinx.rpc.withService
import org.example.krpc.RAILWAY_DOMAIN
import org.example.krpc.TLS_PORT
import org.example.krpc.data.preferences.getToken
import org.example.krpc.di.inject

suspend fun HttpClient.getRpcClient(rpcPath: String): RpcClient {
    val dataStore = inject<DataStore<Preferences>>()
    val token = dataStore.getToken()

    return rpc {
        url {
            protocol = URLProtocol.WSS

            /** railway */
//            host = RAILWAY_DOMAIN
//            port = 0

            /** local tls */
            port = TLS_PORT
            host = getLocalHost()

            /** local http */
//            port = SERVER_PORT
//            host = getLocalHost()

            encodedPath = rpcPath
        }

        rpcConfig {
            serialization {
                json()
            }
        }

        header(HttpHeaders.Authorization, "Bearer $token")
    }
}

suspend inline fun <reified T : RemoteService> HttpClient.rpcService(
    rpcPath: String,
): T {
    val rpcClient = getRpcClient(rpcPath)
    return rpcClient.withService<T>()
}

inline fun <R : Any> rpcFlow(
    crossinline request: suspend () -> Flow<R>
): Flow<R> = channelFlow { /** channel flow т.к. отправляем из другой корутины [streamScoped] */
    streamScoped {
        request().collect {
            send(it)
        }
    }
}
