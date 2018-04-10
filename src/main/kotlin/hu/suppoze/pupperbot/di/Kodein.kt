package hu.suppoze.pupperbot.di

import hu.suppoze.pupperbot.PupperBot
import hu.suppoze.pupperbot.cinema.CinemaScheduleEmbedBuilder
import hu.suppoze.pupperbot.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.cinema.api.OkHttpBaseCinemaApiClient
import hu.suppoze.pupperbot.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.cinema.domain.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.common.http.OkHttpRestClient
import hu.suppoze.pupperbot.common.http.RestClient
import hu.suppoze.pupperbot.giphy.GiphyServer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with


val appModule = Kodein.Module {
    bind<CommandParser>() with singleton { CommandParser() }
    bind<PupperBot>() with singleton { PupperBot() }
}

val cinemaModule = Kodein.Module {
    bind<CinemaScheduleProvider>() with singleton { CinemaScheduleProviderImpl() }
    bind<CinemaScheduleEmbedBuilder>() with singleton { CinemaScheduleEmbedBuilder() }
    bind<CinemaApiClient>() with singleton { OkHttpBaseCinemaApiClient() }
    bind<RestClient>() with singleton { OkHttpRestClient() }
}

val serverModule = Kodein.Module {
    constant("giphyRandomUrl") with "http://api.giphy.com/v1/gifs/random"
    constant("giphySearchUrl") with "http://api.giphy.com/v1/gifs/search"

    bind<GiphyServer>() with singleton { GiphyServer() }
}

val kodein = Kodein {
    import(appModule)
    import(cinemaModule)
    import(serverModule)
}