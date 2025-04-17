package org.example.krpc.models.requests

import kotlinx.serialization.Serializable

@Serializable
class FileChunk(
    val data: ByteArray,
    val chunkIndex: Int,
    val totalChunks: Int
)