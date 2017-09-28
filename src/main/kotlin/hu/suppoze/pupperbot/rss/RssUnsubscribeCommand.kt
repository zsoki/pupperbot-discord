package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein

@ChatCommand(type = AvailableCommands.RSSUNSUB)
class RssUnsubscribeCommand() : UseCase<String> {

    private val rssDatabase: RssDatabase by kodein.instance()
    private val rssService: RssService by kodein.instance()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.message.channel.sendMessage("Successfully unsubscribed from $it feed updates.")
    }


    override val onError: (Throwable) -> Unit = {
        it.printStackTrace()
        parameterizedCommand.event.message.author.orCreatePMChannel.sendMessage("Error during RSS request: ${it.message}")
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

//        rssDatabase.removeSubscription(feedUrl, guildId, channelId)
//                .flatMap { rssDatabase.removeFeedIfNoSubs(it) }
//                .doOnNext { rssService.notifyFeedsChanged() }
//                .subscribe(onNext, onError)
    }
}