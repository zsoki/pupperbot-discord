package hu.suppoze.pupperbot.di

import com.github.salomonbrys.kodein.*
import hu.suppoze.pupperbot.common.PupperBot
import hu.suppoze.pupperbot.common.TokenProvider
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient

val appModule = Kodein.Module {

    bind<IDiscordClient>() with singleton { ClientBuilder().withToken(TokenProvider.token).build() }
    bind<PupperBot>() with singleton { PupperBot(instance()) }

}

val kodein = Kodein.lazy {
    import(appModule)
}
