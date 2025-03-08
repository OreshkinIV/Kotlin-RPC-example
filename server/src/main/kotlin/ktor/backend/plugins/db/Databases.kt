package ktor.backend.ktor.backend.plugins.db

import com.typesafe.config.Config
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

/** Exposed SQL library, поддерживает postgres, mySql и некоторые другие базы */
/** config file resources/application.yaml */
fun Application.configureDatabases(config: Config) {

    /** данные для запуска приложения, развернутого в Railway */

    val url = config.getConfig("storage").getString("jdbcURL")
    val user =  config.getConfig("storage").getString("user")
    val password =  config.getConfig("storage").getString("password")

    /** данные для запуска локально из Android Studio */

//    val url = config.getConfig("storageLocal").getString("jdbcURL")
//    val user =  config.getConfig("storageLocal").getString("user")
//    val password =  config.getConfig("storageLocal").getString("password")

    /** данные для запуска локально из Docker контейнера  */

//    val url = config.getConfig("dockerStorageLocal").getString("jdbcURL")
//    val user =  config.getConfig("dockerStorageLocal").getString("user")
//    val password =  config.getConfig("dockerStorageLocal").getString("password")

    /** jdbc driver для подключения к бд  */
    val driver = config.getConfig("storage").getString("driverClassName")

    Database.connect(
        url = url,
        driver = driver,
        user = user,
        password = password
    )
}