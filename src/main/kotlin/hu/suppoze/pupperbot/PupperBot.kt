package hu.suppoze.pupperbot

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.common.CommandProvider
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.AnnotatedEventManager
import net.dv8tion.jda.core.hooks.SubscribeEvent

class PupperBot {

    companion object : KLogging()

    private val commandParser: CommandParser by kodein.instance()

    lateinit var api: JDA

    fun init() {
        api = JDABuilder(AccountType.BOT)
                .setToken(TokenProvider.token)
                .setGame(Game.of(";help for commands"))
                .setEventManager(AnnotatedEventManager())
                .addEventListener(this)
                .buildBlocking()
    }

    @Suppress("UNUSED_PARAMETER") // Annotations are processed at runtime
    @SubscribeEvent
    private fun onReady(event: ReadyEvent) {
        logger.info { "ReadyEvent received, WOOF! " }
        PupperBotApplication.listenForCommand()
    }

    @SubscribeEvent
    private fun onMessageReceived(event: MessageReceivedEvent) {
        val rawContent = event.message.rawContent
        if (commandParser.isValidCommand(rawContent)) {
            logger.info { "Command received. Raw content='$rawContent'" }
            val parameterizedCommand = commandParser.createParameterizedCommand(event)
            CommandProvider.getUseCaseFor(parameterizedCommand.command)?.execute(parameterizedCommand)
        }
    }
}