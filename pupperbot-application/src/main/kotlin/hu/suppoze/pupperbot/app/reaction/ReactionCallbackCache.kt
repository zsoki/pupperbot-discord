package hu.suppoze.pupperbot.app.reaction

import mu.KLogging
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import java.util.concurrent.ConcurrentHashMap

typealias ReactionCallback = suspend () -> Unit

class ReactionCallbackCache : IReactionCallbackCache {

    companion object : KLogging()

    private val reactionCallbackMap = ConcurrentHashMap<Int, ReactionCallback>()

    override suspend fun executeCachedReaction(messageReactionAddEvent: MessageReactionAddEvent) {
        try {
            val reactionCallbackKey = getHashForMessageReactionEvent(messageReactionAddEvent).hashCode()
            reactionCallbackMap[reactionCallbackKey]?.invoke()
            reactionCallbackMap.remove(reactionCallbackKey)
        } catch (e: Exception) {
            logger.error(e) { "Error during reaction callback: ${e.message}" }
        }
    }

    override fun cacheReactionsForEvent(
        messageReceivedEvent: MessageReceivedEvent,
        callbacksToEmotes: Map<String, ReactionCallback>
    ) {
        callbacksToEmotes.forEach {
            val reactionCallbackKey = getHashForMessageReceivedEvent(messageReceivedEvent, it.key).hashCode()
            reactionCallbackMap.putIfAbsent(reactionCallbackKey, it.value)
        }
    }

    private fun getHashForMessageReceivedEvent(messageReceivedEvent: MessageReceivedEvent, emojiUnicode: String) =
        EventKeyHashArgs(
            guildName = messageReceivedEvent.guild.id,
            textChannelName = messageReceivedEvent.textChannel.id,
            initiatedUser = messageReceivedEvent.author.id,
            emojiUnicode = emojiUnicode
        )

    private fun getHashForMessageReactionEvent(messageReactionAddEvent: MessageReactionAddEvent) = EventKeyHashArgs(
        guildName = messageReactionAddEvent.guild.id,
        textChannelName = messageReactionAddEvent.textChannel.id,
        initiatedUser = messageReactionAddEvent.user.id,
        emojiUnicode = messageReactionAddEvent.reactionEmote.name
    )

    private data class EventKeyHashArgs(
        val guildName: String,
        val textChannelName: String,
        val initiatedUser: String, // TODO: any user in the guild?
        val emojiUnicode: String
    )
}
