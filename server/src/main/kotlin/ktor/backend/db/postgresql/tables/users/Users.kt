package ktor.backend.ktor.backend.db.postgresql.tables.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

/** обращения к бд отключены, на данный момент пробный период railway закончился */
object Users : Table() {
    private val login = Users.varchar("login", 100)
    private val password = Users.varchar("password", 100)

    fun insert(user: User) {
//        transaction {
//            insert {
//                it[login] = user.login
//                it[password] = user.password
//            }
//        }
    }

    fun getUser(login: String): User? {
        return null
//        return try {
//            transaction {
//                val userModel = Users.selectAll().where { Users.login.eq(login) }.single()
//                User(
//                    login = userModel[Users.login],
//                    password = userModel[password],
//                )
//            }
//        } catch (e: Exception) {
//            null
//        }
    }
}
