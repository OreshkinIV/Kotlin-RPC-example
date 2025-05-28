package org.example.krpc.data.network

import android.content.Context
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import org.example.krpc.R
import org.example.krpc.di.inject
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.X509TrustManager

internal actual fun HttpClientConfig<*>.configureForPlatform() {
    engine {
        this as OkHttpConfig
        val context = inject<Context>()
        val certPem = context.resources.openRawResource(R.raw.certificate).readBytes()
        // Загружаем сертификат из файла .pem
        val certFactory = CertificateFactory.getInstance("X.509")
        val certificates = certFactory.generateCertificates(certPem.inputStream())

        // Создаём TrustManager, который доверяет только нашему сертификату
        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                if (!certificates.any { it == chain.first() }) {
                    throw SSLHandshakeException("Untrusted certificate")
                }
            }
            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }

        // Настраиваем SSLContext
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        // Применяем к движку
        config {
            /** если идет обращение к railway, в chain приходит сертификат Lets encrypt, и кастомный trust manager для проверки
             * самоподписанного сертификата не нужен, и надо закомментить sslSocketFactory */
            sslSocketFactory(sslContext.socketFactory, trustManager)
            hostnameVerifier { _, _ -> true } // Отключаем проверку хоста
        }
    }
}