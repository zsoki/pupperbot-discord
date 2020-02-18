package hu.suppoze.pupperbot.app.command.cinema

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.command.common.CommandExecutor
import hu.suppoze.pupperbot.app.reaction.CuratedReactionEmojiPool
import hu.suppoze.pupperbot.app.reaction.ReactionCallback
import hu.suppoze.pupperbot.app.reaction.ReactionCallbackCache
import net.dv8tion.jda.api.entities.MessageEmbed

class CinemaExecutor(
    private val reactionCallbackCache: ReactionCallbackCache,
    private val cinemaScheduleProvider: CinemaScheduleProvider,
    private val cinemaScheduleEmbedBuilder: CinemaScheduleEmbedBuilder
) : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
        val schedule = cinemaScheduleProvider.fetchNextWeekSchedule("szeged")

        val reactionEmojiPool = CuratedReactionEmojiPool()
        val emoteToScreening = schedule.screenings.associateBy { reactionEmojiPool.getNext() }
        val emoteToCallbackMap = emoteToScreening
            .mapValues { cinemaScheduleEmbedBuilder.buildScreeningTimeEmbed(it.value) } // TODO operators could be merged
            .mapValues { emoteToEmbed -> createReactionCallback(commandContext, emoteToEmbed) }

        val movieEmbed = cinemaScheduleEmbedBuilder.buildMovieListEmbed(schedule.cinemaName, emoteToScreening)
        reactionCallbackCache.cacheReactionsForEvent(commandContext.event, emoteToCallbackMap)

        val sentMessage = commandContext.event.textChannel.sendMessage(movieEmbed).complete()
        emoteToScreening.forEach { sentMessage.addReaction(it.key).queue() }
    }

    private fun createReactionCallback(commandContext: CommandContext, emoteToEmbed: Map.Entry<String, MessageEmbed>): ReactionCallback = {
        commandContext.event.textChannel.sendMessage(emoteToEmbed.value).queue()
    }
}
