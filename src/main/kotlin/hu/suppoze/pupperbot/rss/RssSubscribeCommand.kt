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

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (RssSubscriptionDao) -> Unit = {
        parameterizedCommand.event.message.channel.sendMessage("Subscribed to ${it.feed.title} RSS in channel #${parameterizedCommand.event.channel.name}")
    }

    override val onError: (Throwable) -> Unit = {
        parameterizedCommand.event.message.channel.sendMessage("Cannot subscribe: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        if (parameterizedCommand.params == null || parameterizedCommand.params.isEmpty()) {
            onError(IllegalArgumentException("You need to add an RSS feed URL as a parameter"))
            return
        }

        val feedUrl = parameterizedCommand.params[0]
        val guildId = parameterizedCommand.event.guild.longID
        val channelId = parameterizedCommand.event.channel.longID

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