package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.CommandLibrary
import hu.suppoze.pupperbot.app.command.CommandParser
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
import kotlin.coroutines.CoroutineContext

class PupperBot(
    private val commandLibrary: CommandLibrary,
    private val reactionCallbackCache: ReactionCallbackCache
) : CoroutineScope {

    private val commandParser = CommandParser(commandLibrary)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    companion object : KLogging()

    lateinit var api: JDA

    fun init() {
        api = JDABuilder(AccountType.BOT)
            .setToken(pupperConfiguration.token)
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
            val commandContext = commandParser.buildCommandContext(event)
            commandLibrary.getCommandBy(commandContext.keyword).execute(commandContext)
        }
    }

    @Suppress("unused")
    @SubscribeEvent
    private fun onMessageReceived(event: MessageReactionAddEvent) = launch {
        if (event.reaction.isSelf) return@launch
        reactionCallbackCache.executeCachedReaction(event)
    }
}