package hu.suppoze.pupperbot.app.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.suppoze.pupperbot.app.PupperBot
import hu.suppoze.pupperbot.app.command.cinema.CinemaScheduleEmbedBuilder
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiClientImpl
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.app.command.CommandParser
import hu.suppoze.pupperbot.app.http.OkHttpRestClient
import hu.suppoze.pupperbot.app.http.RestClient
import hu.suppoze.pupperbot.app.command.giphy.api.GiphyClient
import hu.suppoze.pupperbot.app.command.giphy.api.GiphyClientImpl
import hu.suppoze.pupperbot.app.command.spawnalert.SpawnAlertImporter
import hu.suppoze.pupperbot.app.command.spawnalert.SpawnAlertImporterImpl
import hu.suppoze.pupperbot.app.command.spawnalert.SpawnAlertScheduler
import hu.suppoze.pupperbot.app.command.spawnalert.SpawnAlertSchedulerImpl
import org.jetbrains.exposed.sql.Database
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with


val appModule = Kodein.Module("appModule") {
    bind<CommandParser>() with singleton { CommandParser() }
    bind<PupperBot>() with singleton { PupperBot() }
    bind<Gson>() with singleton { GsonBuilder().create() }
}

val cinemaModule = Kodein.Module("cinemaModule") {
    constant("cinemaListRequestUrl") with "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/{{0}}"
    constant("cinemaFilmEventsRequestUrl") with "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/film-events/in-cinema/{{0}}/at-date/{{1}}"
    constant("cinemaCityLogoUrl") with "https://www.cinemacity.hu/xmedia/img/10102/default-placeholder.png"

    bind<CinemaScheduleProvider>() with singleton { CinemaScheduleProviderImpl() }
    bind<CinemaScheduleEmbedBuilder>() with singleton { CinemaScheduleEmbedBuilder() }
    bind<CinemaApiClient>() with singleton { CinemaApiClientImpl() }
    bind<RestClient>() with singleton { OkHttpRestClient() }
}

val giphyModule = Kodein.Module("giphyModule") {
    constant("giphyRandomUrl") with "http://api.giphy.com/v1/gifs/random"
    constant("giphySearchUrl") with "http://api.giphy.com/v1/gifs/search"

    bind<GiphyClient>() with singleton { GiphyClientImpl() }
}

val spawnAlertModule = Kodein.Module("spawnAlertModule") {
    constant("spawnAlertScheduleResource") with "spawnAlertSchedule.csv"

    bind<SpawnAlertImporter>() with singleton { SpawnAlertImporterImpl() }
    bind<SpawnAlertScheduler>() with singleton { SpawnAlertSchedulerImpl() }
}

val databaseModule = Kodein.Module("databaseModule") {
    constant("dbInstance") with Database.connect("jdbc:sqlite/data/data.db", driver = "org.sqlite.JDBC")
}

val kodein = Kodein {
    import(appModule)
    import(cinemaModule)
    import(giphyModule)
    import(spawnAlertModule)
    import(databaseModule)
}