package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.rss.model.RssEntry
import io.reactivex.schedulers.Schedulers
import sx.blah.discord.util.EmbedBuilder
import java.net.URL

class RssCommand(val rawCommand: CommandParser.RawCommand) : UseCase<RssEntry> {

    private val rssDatabase: RssDatabase by kodein.instance()
    private val rssServer: RssServer by kodein.instance()

    override val onNext: (RssEntry) -> Unit = {
        val embed = EmbedBuilder()
                .withTitle(it.title)
                .withAuthorName(it.author)
                .withDesc(it.description)
                .withUrl(it.link)
        rawCommand.event.message.channel.sendMessage("", embed.build(), false)
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during RSS request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute() {
        if (rawCommand.parameters == null || rawCommand.parameters.isEmpty()) {
            onError (IllegalArgumentException("You need to add an RSS feed URL as a parameter"))
            return
        }

        // TODO: build observable
//        rssServer.getFeed(URL(rawCommand.parameters[0]))
//                .subscribeOn(Schedulers.io())
//                .flatMap { feed -> rssDatabase.persistFeed(feed) }
//                .observeOn(Schedulers.trampoline())
//                .subscribe(onNext, onError)
    }
}