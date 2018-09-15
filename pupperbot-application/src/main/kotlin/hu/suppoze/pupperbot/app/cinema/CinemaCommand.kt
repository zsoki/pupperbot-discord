package hu.suppoze.pupperbot.app.cinema

import hu.suppoze.pupperbot.app.cinema.domain.CinemaScheduleProvider
import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase
import hu.suppoze.pupperbot.app.di.kodein
import org.kodein.di.generic.instance

@ChatCommand(type = AvailableCommands.CINEMA_CITY)
class CinemaCommand : UseCase() {

    private val cinemaScheduleProvider: CinemaScheduleProvider by kodein.instance()
    private val cinemaScheduleEmbedBuilder: CinemaScheduleEmbedBuilder by kodein.instance()

    override fun onExecute() {
        val schedule = cinemaScheduleProvider.fetchNextWeekSchedule("szeged") // TODO hardcoded value
        val embed = cinemaScheduleEmbedBuilder.buildScheduleEmbed(schedule)
        commandContext.event.textChannel.sendMessage(embed).queue()
    }

}