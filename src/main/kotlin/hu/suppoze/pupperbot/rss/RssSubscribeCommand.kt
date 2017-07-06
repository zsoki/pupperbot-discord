package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.rss.model.RssSubscriptionDao

@ChatCommand(type = AvailableCommands.RSSSUB)
class RssSubscribeCommand : UseCase<RssSubscriptionDao> {

    private val rssDatabase: RssDatabase by kodein.instance()
    private val rssServer: RssServer by kodein.instance()
    private val rssService: RssService by kodein.instance()

    private lateinit var rawCommand: RawCommand

    override val onNext: (RssSubscriptionDao) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Subscribed to ${it.feed.title} RSS in channel #${rawCommand.event.channel.name}")
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Cannot subscribe: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(rawCommand: RawCommand) {
        this.rawCommand = rawCommand

        if (rawCommand.parameters == null || rawCommand.parameters.isEmpty()) {
            onError(IllegalArgumentException("You need to add an RSS feed URL as a parameter"))
            return
        }

        val feedUrl = rawCommand.parameters[0]
        val guildId = rawCommand.event.guild.longID
        val channelId = rawCommand.event.channel.longID

        rssDatabase.getFeedByUrl(feedUrl)
                .flatMap { rssDatabase.saveSubscriptionIfNotExists(guildId, channelId, it) }
                .switchIfEmpty {
                    rssServer.getFeed(feedUrl)
                            .flatMap { syndFeed -> rssDatabase.persistFeed(syndFeed, feedUrl) }
                            .flatMap { dbFeed -> rssDatabase.saveSubscriptionIfNotExists(guildId, channelId, dbFeed) }
                }
                .map {
                    rssService.subscriptionAdded(it)
                    it
                }
                .subscribe(onNext, onError)
    }
}