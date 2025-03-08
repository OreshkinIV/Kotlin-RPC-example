package org.example.krpc.data.network

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

internal actual fun HttpClientConfig<*>.configureForPlatform() {
    engine {
        this as OkHttpConfig
        config {
            hostnameVerifier { hostname, session -> true }
            val trustAllCert = AllCertsTrustManager()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustAllCert), SecureRandom())
            sslSocketFactory(sslContext.socketFactory, trustAllCert)
        }
    }
}

@Suppress("CustomX509TrustManager")
internal class AllCertsTrustManager : X509TrustManager {

    @Suppress("TrustAllX509TrustManager")
    override fun checkServerTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
    }

    @Suppress("TrustAllX509TrustManager")
    override fun checkClientTrusted(
        chain: Array<X509Certificate>,
        authType: String
    ) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}