package hu.suppoze.pupperbot.cinema

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase
import mu.KLogging
import org.jsoup.Jsoup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ChatCommand(type = AvailableCommands.CINEMA_CITY)
class CinemaCommand : UseCase<String> {

    companion object : KLogging()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        val locationId = "101021"
        val date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        val document = Jsoup.connect("http://www.cinemacity.hu/scheduleInfo")
                .data(mutableMapOf(
                        "locationId" to locationId,
                        "date" to date,
                        "venueTypeId" to "0"))
                .post()

        val message = document.select("tbody > tr")
                .map {
                    Movie(
                            title = it.child(0).text(),
                            type = it.child(2).ownText(),
                            language = it.child(3).ownText(),
                            length = it.child(4).text(),
                            times = it.select("td > a.presentationLink")
                                    .filter { it.hasText() }
                                    .map { it.text() }
                    )
                }
                .map {
                    val typeString = if (!it.type.isEmpty()) " (${it.type})" else ""
                    val languageString = if (!it.language.isEmpty()) " (${it.language})" else ""
                    val timesString = it.times.reduce {f, s -> "$f, $s"}

                    "**${it.title}***$typeString$languageString (${it.length} perc)* - $timesString"
                }
                .fold("**Cinema City Szeged filmek ekkor: $date**\n") {fRow, sRow -> "$fRow\n$sRow"}

        parameterizedCommand.event.textChannel.sendMessage(message).queue()
    }

    private data class Movie(val title: String,
                             val type: String,
                             val language: String,
                             val length: String,
                             val times: List<String>)
}