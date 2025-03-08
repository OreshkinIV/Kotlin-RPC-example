package org.example.krpc.models.responses

import kotlinx.serialization.Serializable

@Serializable
class RpcResponse<T: Any?> (
    val data: T? = null,
    val error: ErrorResponse? = null
)

fun <T: Any?> RpcResponse<T?>.getOrError(): T? {
    return if (!error?.message.isNullOrBlank()) {
        throw Throwable(error?.message)
    } else {
        data
    }
}
