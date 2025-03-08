package ktor.backend.ktor.backend.db.postgresql.ext

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun Table.deleteTable() {
    transaction {
        dropStatement().forEach { statement ->
            exec(statement)
        }
    }
}
