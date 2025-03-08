package org.example.krpc.data.api.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.UserResponse

interface RestApi {
    suspend fun registerUser(authBody: AuthBody): UserResponse
}

suspend inline fun <reified T : Any> HttpClient.postRequest(url: String, body: Any?) =
    post(url) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body<T>()


/** rest POST /register example */
class RestApiImpl(private val httpClient: HttpClient) : RestApi {

    override suspend fun registerUser(authBody: AuthBody): UserResponse {
        return httpClient.postRequest("register", authBody)
    }
}