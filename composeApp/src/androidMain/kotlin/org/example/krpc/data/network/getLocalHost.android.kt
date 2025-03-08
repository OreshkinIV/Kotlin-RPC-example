package org.example.krpc.data.network

/** при запуске сервера локально из студии использовать 10.0.2.2 - хост для эмуляторов */
actual fun getLocalHost(): String {
    return "10.0.2.2"
}