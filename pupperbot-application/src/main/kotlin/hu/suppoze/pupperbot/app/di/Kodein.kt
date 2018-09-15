package hu.suppoze.pupperbot.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.suppoze.pupperbot.app.PupperBot
import hu.suppoze.pupperbot.app.cinema.CinemaScheduleEmbedBuilder
import hu.suppoze.pupperbot.app.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.app.cinema.api.CinemaApiClientImpl
import hu.suppoze.pupperbot.app.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.cinema.domain.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.app.common.command.CommandParser
import hu.suppoze.pupperbot.app.common.http.OkHttpRestClient
import hu.suppoze.pupperbot.app.common.http.RestClient
import hu.suppoze.pupperbot.app.giphy.api.GiphyClient
import hu.suppoze.pupperbot.app.giphy.api.GiphyClientImpl
import hu.suppoze.pupperbot.app.spawnalert.SpawnAlertImporter
import hu.suppoze.pupperbot.app.spawnalert.SpawnAlertImporterImpl
import hu.suppoze.pupperbot.app.spawnalert.SpawnAlertScheduler
import hu.suppoze.pupperbot.app.spawnalert.SpawnAlertSchedulerImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with


val appModule = Kodein.Module {
    bind<CommandParser>() with singleton { CommandParser() }
    bind<PupperBot>() with singleton { PupperBot() }
    bind<Gson>() with singleton { GsonBuilder().create() }
}

val cinemaModule = Kodein.Module {
    constant("cinemaListRequestUrl") with "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/{{0}}"
    constant("cinemaFilmEventsRequestUrl") with "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/film-events/in-cinema/{{0}}/at-date/{{1}}"
    constant("cinemaCityLogoUrl") with "https://www.cinemacity.hu/xmedia/img/10102/default-placeholder.png"

    bind<CinemaScheduleProvider>() with singleton { CinemaScheduleProviderImpl() }
    bind<CinemaScheduleEmbedBuilder>() with singleton { CinemaScheduleEmbedBuilder() }
    bind<CinemaApiClient>() with singleton { CinemaApiClientImpl() }
    bind<RestClient>() with singleton { OkHttpRestClient() }
}

val giphyModule = Kodein.Module {
    constant("giphyRandomUrl") with "http://api.giphy.com/v1/gifs/random"
    constant("giphySearchUrl") with "http://api.giphy.com/v1/gifs/search"

    bind<GiphyClient>() with singleton { GiphyClientImpl() }
}

val spawnAlertModule = Kodein.Module {
    constant("spawnAlertScheduleFileLocation") with "./spawnAlertSchedule.csv"

    bind<SpawnAlertImporter>() with singleton { SpawnAlertImporterImpl() }
    bind<SpawnAlertScheduler>() with singleton { SpawnAlertSchedulerImpl() }
}

val kodein = Kodein {
    import(appModule)
    import(cinemaModule)
    import(giphyModule)
    import(spawnAlertModule)
}