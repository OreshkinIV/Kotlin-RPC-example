package org.example.krpc.data.network

/** todo может отличаться от хоста для декстопа и андройда */
actual fun getLocalHost(): String {
    return "0.0.0.0"
}