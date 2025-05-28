package org.example.krpc.data.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.rpc.RemoteService
import kotlinx.rpc.RpcClient
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService
import org.example.krpc.RAILWAY_DOMAIN
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
            port = 8443
            host = getLocalHost()

            /** local http */
//            port = 8080
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
