package hu.suppoze.pupperbot.app.reaction

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent

interface IReactionCallbackCache {

    suspend fun executeCachedReaction(messageReactionAddEvent: MessageReactionAddEvent)

    fun cacheReactionsForEvent(messageReceivedEvent: MessageReceivedEvent, callbacksToEmotes: Map<String, ReactionCallback>)

}
