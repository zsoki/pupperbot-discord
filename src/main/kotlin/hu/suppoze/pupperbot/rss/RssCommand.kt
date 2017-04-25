package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.rss.model.RssSubscription
import java.net.URL

class RssCommand(val rawCommand: CommandParser.RawCommand) : UseCase<RssSubscription> {

    private val rssDatabase: RssDatabase by kodein.instance()
    private val rssServer: RssServer by kodein.instance()

    override val onNext: (RssSubscription) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Subscribed to ${it.feed.title} RSS in channel #${rawCommand.event.channel.name}")
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

        val feedUrl = rawCommand.parameters[0]
        val guildId = rawCommand.event.guild.longID
        val channelId = rawCommand.event.channel.longID

        rssServer.getFeed(URL(feedUrl))
                .flatMap { syndFeed -> rssDatabase.persistFeed(syndFeed, feedUrl) }
                .flatMap { dbFeed -> rssDatabase.persistSubscription(guildId, channelId, dbFeed) }
                .subscribe(onNext, onError)
    }
}