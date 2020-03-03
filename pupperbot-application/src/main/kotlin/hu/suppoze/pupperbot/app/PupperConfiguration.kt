package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient
import hu.suppoze.pupperbot.app.command.gif.api.GiphyClient
import hu.suppoze.pupperbot.app.http.PupperRestClient
import java.io.FileReader

val pupperConfiguration = pupperConfiguration {
    token { FileReader("./token.txt").readText() }
}

data class PupperConfiguration(
    val token: String
)

private inline fun pupperConfiguration(lambda: PupperConfigurationBuilder.() -> Unit): PupperConfiguration {
    return PupperConfigurationBuilder().apply(lambda).build()
}

class PupperConfigurationBuilder {
    lateinit var token: String

    inline fun token(provider: () -> String) { token = provider() }

    fun build() = PupperConfiguration(token)
}
