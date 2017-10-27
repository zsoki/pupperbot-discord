package hu.suppoze.pupperbot.di

import com.github.salomonbrys.kodein.*
import hu.suppoze.pupperbot.PupperBot
import hu.suppoze.pupperbot.cinema.CinemaService
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.giphy.GiphyServer


val appModule = Kodein.Module {
    bind<CommandParser>() with singleton { CommandParser() }
    bind<PupperBot>() with singleton { PupperBot() }
}

val serviceModule = Kodein.Module {
    bind<CinemaService>() with singleton { CinemaService() }
}

val serverModule = Kodein.Module {
    constant("giphyRandomUrl") with "http://api.giphy.com/v1/gifs/random"
    constant("giphySearchUrl") with "http://api.giphy.com/v1/gifs/search"

    bind<GiphyServer>() with singleton { GiphyServer() }
}

val kodein = Kodein.lazy {
    import(appModule)
    import(serviceModule)
    import(serverModule)
}