package org.example.krpc.models.responses

import io.ktor.http.HttpStatusCode

class ErrorResponse(
    val httpStatusCode: HttpStatusCode,
    message: String,
) : Exception(message)

class CustomErrorResponse(
    val errorCode: Int,
    val message: String,
)

class RpcResponse<T>(
    val data: T? = null,
    val error: CustomErrorResponse? = null
)