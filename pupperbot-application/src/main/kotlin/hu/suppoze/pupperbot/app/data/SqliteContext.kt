package hu.suppoze.pupperbot.app.data

import hu.suppoze.pupperbot.app.spawnalert.repository.ScheduleStringTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object SqliteContext {
    val db: Database =
        Database.connect("jdbc:sqlite:data/data.db", driver = "org.sqlite.JDBC")

    init {
        TransactionManager.manager.defaultIsolationLevel =
                Connection.TRANSACTION_SERIALIZABLE
    }

    init {
        transaction {
            SchemaUtils.create(ScheduleStringTable)
        }
    }
}