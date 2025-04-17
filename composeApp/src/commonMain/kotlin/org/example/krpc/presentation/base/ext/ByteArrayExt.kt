package org.example.krpc.presentation.base.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun ByteArray.asFlow(chunkSize: Int): Flow<ByteArray> = flow {
    for (i in 0 until size step chunkSize) {
        val end = if (i + chunkSize > size) size else i + chunkSize
        emit(copyOfRange(i, end))
    }
}