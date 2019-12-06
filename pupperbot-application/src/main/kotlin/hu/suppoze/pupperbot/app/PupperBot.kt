package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.CommandParser
import hu.suppoze.pupperbot.app.command.CommandProvider
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.reaction.ReactionCallbackCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mu.KLogging
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.hooks.SubscribeEvent
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

class PupperBot : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    companion object : KLogging()

    private val commandParser: CommandParser by kodein.instance()
    private val reactionCallbackCache: ReactionCallbackCache by kodein.instance()

    lateinit var api: JDA

    fun init() {
        api = JDABuilder(AccountType.BOT)
            .setToken(TokenProvider.token)
            .setActivity(Activity.playing(";help for commands"))
            .setEventManager(AnnotatedEventManager())
            .addEventListeners(this)
            .build().awaitReady()
    }

    @Suppress("unused_parameter", "unused")
    @SubscribeEvent
    private fun onReady(event: ReadyEvent) {
        logger.info { "ReadyEvent received, WOOF! " }
        PupperBotApplication.listenForCommand()
    }

    @Suppress("unused")
    @SubscribeEvent
    private fun onMessageReceived(event: MessageReceivedEvent) = launch {
        val rawContent = event.message.contentRaw
        if (commandParser.isValidCommand(rawContent)) {
            logger.info { "Command received. Raw content='$rawContent'" }
            val commandContext = commandParser.buildCommandContext(event)
            CommandProvider.getUseCaseFor(commandContext.rawCommand)?.executeAsync(commandContext)
        }
    }

    @Suppress("unused")
    @SubscribeEvent
    private fun onMessageReceived(event: MessageReactionAddEvent) = launch {
        if (event.reaction.isSelf) return@launch
        reactionCallbackCache.executeCachedReaction(event)
    }
}