package hu.suppoze.pupperbot.common

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.di.kodein
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.sqlite.SQLiteConnection
import sx.blah.discord.api.IDiscordClient

class PupperBot(val client: IDiscordClient) {

    private val databaseUrl: String by kodein.instance("databaseUrl")
    private val databaseDriver: String by kodein.instance("databaseDriver")

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        Database.connect(databaseUrl, driver = databaseDriver)
        TransactionManager.manager.defaultIsolationLevel = SQLiteConnection.TRANSACTION_SERIALIZABLE
    }
}