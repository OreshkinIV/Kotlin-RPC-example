package org.example.krpc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform