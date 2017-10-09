package hu.suppoze.pupperbot.di

import com.github.salomonbrys.kodein.*
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.PupperBot
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.giphy.GiphyServer
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient

val appModule = Kodein.Module {
    bind<IDiscordClient>() with singleton {
        ClientBuilder()
                .withToken(TokenProvider.token)
                .withPingTimeout(3)
                .login()
    }
    bind<PupperBot>() with singleton { PupperBot(instance()) }
    bind<CommandParser>() with singleton { CommandParser() }
}

val serverModule = Kodein.Module {
    constant("giphyRandomUrl") with "http://api.giphy.com/v1/gifs/random"
    constant("giphySearchUrl") with "http://api.giphy.com/v1/gifs/search"

    bind<GiphyServer>() with singleton { GiphyServer() }
}

val kodein = Kodein.lazy {
    import(appModule)
    import(serverModule)
}