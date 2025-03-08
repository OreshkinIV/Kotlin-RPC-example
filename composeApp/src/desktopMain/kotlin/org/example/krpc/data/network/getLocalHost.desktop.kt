package org.example.krpc.data.network

/** при запуске сервера локально из студии использовать 0.0.0.0 */
actual fun getLocalHost(): String {
    return "0.0.0.0"
}