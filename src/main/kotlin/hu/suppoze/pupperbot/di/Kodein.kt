package hu.suppoze.pupperbot.di

import hu.suppoze.pupperbot.PupperBot
import hu.suppoze.pupperbot.cinema.CinemaScheduleEmbedBuilder
import hu.suppoze.pupperbot.cinema.CinemaScheduleProvider
import hu.suppoze.pupperbot.cinema.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.cinema.api.CinemaApi
import hu.suppoze.pupperbot.cinema.api.CinemaApiImpl
import hu.suppoze.pupperbot.common.CommandParser
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
    constant("cinemaApiUrl") with "https://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/"
    bind<CinemaScheduleProvider>() with singleton { CinemaScheduleProviderImpl() }
    bind<CinemaScheduleEmbedBuilder>() with singleton { CinemaScheduleEmbedBuilder() }
    bind<CinemaApi>() with singleton { CinemaApiImpl() }
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