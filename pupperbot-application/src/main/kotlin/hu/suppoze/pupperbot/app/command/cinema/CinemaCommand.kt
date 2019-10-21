package hu.suppoze.pupperbot.app.command.cinema

import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase
import hu.suppoze.pupperbot.app.command.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.reaction.CuratedReactionEmojiPool
import hu.suppoze.pupperbot.app.reaction.ReactionCallback
import hu.suppoze.pupperbot.app.reaction.ReactionCallbackCache
import net.dv8tion.jda.core.entities.MessageEmbed
import org.kodein.di.generic.instance

@ChatCommand(type = AvailableCommands.CINEMA_CITY)
class CinemaCommand : UseCase() {

    private val reactionCallbackCache: ReactionCallbackCache by kodein.instance()
    private val cinemaScheduleProvider: CinemaScheduleProvider by kodein.instance()
    private val cinemaScheduleEmbedBuilder: CinemaScheduleEmbedBuilder by kodein.instance()

    override suspend fun onExecute() {
        val schedule = cinemaScheduleProvider.fetchNextWeekSchedule("szeged") // TODO hardcoded value

        val reactionEmojiPool = CuratedReactionEmojiPool()
        val emoteToScreening = schedule.screenings.associateBy { reactionEmojiPool.getNext() }
        val emoteToCallbackMap = emoteToScreening
            .mapValues { cinemaScheduleEmbedBuilder.buildScreeningTimeEmbed(it.value) }
            .mapValues { createReactionCallback(it) }

        val movieEmbed = cinemaScheduleEmbedBuilder.buildMovieListEmbed(schedule.cinemaName, emoteToScreening)
        reactionCallbackCache.cacheReactionsForEvent(commandContext.event, emoteToCallbackMap)

        val sentMessage = commandContext.event.textChannel.sendMessage(movieEmbed).complete()
        emoteToScreening.forEach { sentMessage.addReaction(it.key).queue() }
    }

    private fun createReactionCallback(emoteToEmbed: Map.Entry<String, MessageEmbed>): ReactionCallback = {
        commandContext.event.textChannel.sendMessage(emoteToEmbed.value).queue()
    }

}