package org.example.krpc.data.network

import android.annotation.SuppressLint
import android.content.Context
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import org.example.krpc.R
import org.example.krpc.di.inject
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
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

//            sslSocketFactory(SslSettings.getSslContext()!!.socketFactory, SslSettings.getTrustManager())
        }
    }
}

@SuppressLint("CustomX509TrustManager")
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

object SslSettings {
    private fun getKeyStore(): KeyStore {
        val context = inject<Context>()
        val keyStoreFile = context.resources.openRawResource(R.raw.rpcexample)
        val keyStorePassword = "password".toCharArray()
        val keyStore: KeyStore = KeyStore.getInstance("BKS")
        keyStore.load(keyStoreFile, keyStorePassword)
        return keyStore
    }

    private fun getTrustManagerFactory(): TrustManagerFactory? {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(getKeyStore())
        return trustManagerFactory
    }

    fun getSslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, getTrustManagerFactory()?.trustManagers, null)
        return sslContext
    }

    fun getTrustManager(): X509TrustManager {
        return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
    }
}