package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein

@ChatCommand(type = AvailableCommands.RSSUNSUB)
class RssUnsubscribeCommand() : UseCase<String> {

    private val rssDatabase: RssDatabase by kodein.instance()
    private val rssService: RssService by kodein.instance()

    private lateinit var rawCommand: RawCommand

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Successfully unsubscribed from $it feed updates.")
    }


    override val onError: (Throwable) -> Unit = {
        it.printStackTrace()
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during RSS request: ${it.message}")
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

//        rssDatabase.removeSubscription(feedUrl, guildId, channelId)
//                .flatMap { rssDatabase.removeFeedIfNoSubs(it) }
//                .doOnNext { rssService.notifyFeedsChanged() }
//                .subscribe(onNext, onError)
    }
}