package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.CommandLibrary
import hu.suppoze.pupperbot.app.command.cinema.CinemaScheduleEmbedBuilder
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiClientImpl
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient
import hu.suppoze.pupperbot.app.command.gif.api.GiphyClient
import hu.suppoze.pupperbot.app.http.OkHttpPupperRestClient
import hu.suppoze.pupperbot.app.http.PupperRestClient
import hu.suppoze.pupperbot.app.reaction.ReactionCallbackCache
import java.io.FileReader

val pupperConfiguration = pupperConfiguration {
    token { FileReader("./token.txt").readText() }
    restClient { OkHttpPupperRestClient() }
    gifClient { GiphyClient(restClient) }
    reactionCallbackCache { ReactionCallbackCache() }
    cinemaClient { CinemaApiClientImpl(restClient) }
    cinemaScheduleProvider { CinemaScheduleProviderImpl(cinemaClient) }
    cinemaScheduleEmbedBuilder { CinemaScheduleEmbedBuilder() }
}

data class PupperConfiguration(
    val token: String,
    val restClient: PupperRestClient,
    val gifClient: PupperGifClient,
    val reactionCallbackCache: ReactionCallbackCache,
    val cinemaClient: CinemaApiClient,
    val cinemaScheduleProvider: CinemaScheduleProvider,
    val cinemaScheduleEmbedBuilder: CinemaScheduleEmbedBuilder
)

private inline fun pupperConfiguration(lambda: PupperConfigurationBuilder.() -> Unit): PupperConfiguration {
    return PupperConfigurationBuilder().apply(lambda).build()
}

class PupperConfigurationBuilder {
    lateinit var token: String
    lateinit var restClient: PupperRestClient
    lateinit var gifClient: PupperGifClient
    lateinit var reactionCallbackCache: ReactionCallbackCache
    lateinit var cinemaClient: CinemaApiClient
    lateinit var cinemaScheduleProvider: CinemaScheduleProvider
    lateinit var cinemaScheduleEmbedBuilder: CinemaScheduleEmbedBuilder

    inline fun token(provider: () -> String) { token = provider() }
    inline fun restClient(provider: () -> PupperRestClient) { restClient = provider() }
    inline fun gifClient(provider: () -> PupperGifClient) { gifClient = provider() }
    inline fun reactionCallbackCache(provider: () -> ReactionCallbackCache) { reactionCallbackCache = provider() }
    inline fun cinemaClient(provider: () -> CinemaApiClient) { cinemaClient = provider() }
    inline fun cinemaScheduleProvider(provider: () -> CinemaScheduleProvider) { cinemaScheduleProvider = provider() }
    inline fun cinemaScheduleEmbedBuilder(provider: () -> CinemaScheduleEmbedBuilder) { cinemaScheduleEmbedBuilder = provider() }

    fun build() = PupperConfiguration(token, restClient, gifClient, reactionCallbackCache, cinemaClient, cinemaScheduleProvider, cinemaScheduleEmbedBuilder)
}
