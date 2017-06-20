package hu.suppoze.pupperbot.di

import com.github.salomonbrys.kodein.*
import hu.suppoze.pupperbot.common.CommandFactory
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.common.PupperBot
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.giphy.GiphyServer
import hu.suppoze.pupperbot.rss.RssDatabase
import hu.suppoze.pupperbot.rss.RssServer
import hu.suppoze.pupperbot.rss.RssService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.sqlite.SQLiteConnection
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient

val appModule = Kodein.Module {
    bind<IDiscordClient>() with singleton { ClientBuilder().withToken(TokenProvider.token).login() }
    bind<PupperBot>() with singleton { PupperBot(instance()) }
    bind<CommandFactory>() with singleton { CommandFactory() }
    bind<CommandParser>() with singleton { CommandParser() }
}

val databaseModule = Kodein.Module {
    constant("databaseUrl") with "jdbc:sqlite:pupperbot.sqlite"
    constant("databaseDriver") with "org.sqlite.JDBC"

    bind<RssDatabase>() with singleton { RssDatabase() }
}

val serverModule = Kodein.Module {
    constant("giphyUrl") with "http://api.giphy.com/v1/gifs/random"

    bind<GiphyServer>() with singleton { GiphyServer() }
    bind<RssServer>() with singleton { RssServer() }
    bind<RssService>() with singleton { RssService(instance(), instance()) }
}

val kodein = Kodein.lazy {
    import(appModule)
    import(databaseModule)
    import(serverModule)
}