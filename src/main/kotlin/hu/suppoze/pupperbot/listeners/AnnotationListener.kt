package hu.suppoze.pupperbot.listeners

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.PupperBotApplication
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.common.CommandProvider
import hu.suppoze.pupperbot.PupperBot
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent
import sx.blah.discord.handle.impl.events.shard.ReconnectFailureEvent
import sx.blah.discord.handle.impl.events.shard.ReconnectSuccessEvent
import sx.blah.discord.handle.impl.events.shard.ResumedEvent

class AnnotationListener {

    companion object : KLogging()

    private val pupperBot: PupperBot by kodein.instance()
    private val commandParser: CommandParser by kodein.instance()

    @EventSubscriber
    fun onReadyEvent(event: ReadyEvent) {
        logger.info { "ReadyEvent received" }
        pupperBot.client.online(";help for commands")
        PupperBotApplication.listenForCommand()
    }

    @EventSubscriber
    fun onMessageReceiedEvent(event: MessageReceivedEvent) {
        val rawContent = event.message.content
        if (rawContent.matches(Regex("^;[\\w\\s\\d]+ .*"))) {
            logger.info { "Command received. Raw content='$rawContent'" }
            val parameterizedCommand = commandParser.parse(event)
            CommandProvider.get(parameterizedCommand.command)?.execute(parameterizedCommand)
        }
    }

    @EventSubscriber
    fun onDisconnectEvent(event: DisconnectedEvent) {
        logger.info { "DisconnectedEvent received, reason=${event.reason} " }
    }

    @EventSubscriber
    fun onReconnectFailureEvent(event: ReconnectFailureEvent) {
        logger.warn { "ReconnectFailureEvent received, isShardAbandoned=${event.isShardAbandoned}, currentAttempt=${event.currentAttempt} " }
    }

    @EventSubscriber
    fun onReconnectSuccessEvent(event: ReconnectSuccessEvent) {
        logger.info { "ReconnectSuccessEvent received" }
    }

    @EventSubscriber
    fun onResumedEvent(event: ResumedEvent) {
        logger.info { "ResumedEvent received" }
    }
}