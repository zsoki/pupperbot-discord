package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient
import hu.suppoze.pupperbot.app.command.gif.api.GiphyClient
import hu.suppoze.pupperbot.app.http.PupperRestClient
import java.io.FileReader

val pupperConfiguration = pupperConfiguration {
    token { FileReader("./token.txt").readText() }
    gifClient { GiphyClient(restClient) }
}

data class PupperConfiguration(
    val token: String,
    val restClient: PupperRestClient,
    val gifClient: PupperGifClient
)

private inline fun pupperConfiguration(lambda: PupperConfigurationBuilder.() -> Unit): PupperConfiguration {
    return PupperConfigurationBuilder().apply(lambda).build()
}

class PupperConfigurationBuilder {
    lateinit var token: String
    lateinit var restClient: PupperRestClient
    lateinit var gifClient: PupperGifClient

    inline fun token(provider: () -> String) { token = provider() }
    inline fun restClient(provider: () -> PupperRestClient) { restClient = provider() }
    inline fun gifClient(provider: () -> PupperGifClient) { gifClient = provider() }

    fun build() = PupperConfiguration(token, restClient, gifClient)
}
