package hu.suppoze.pupperbot.cinema

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// TODO: dependency inversion
class CinemaScheduleEmbedBuilder {

    fun buildScheduleEmbed(schedule: Schedule) : MessageEmbed {
        val builder = EmbedBuilder()
            .setTitle("Cinema City Szeged Műsor")
            .setDescription(createDescriptionText())
            .setColor(Color.ORANGE)
            .setImage("http://www.cinemacity.hu/media/iti_cz/imgs/cc_logo.png")
        for (screening in schedule.screenings) {
            builder.addField(
                screening.key.title,
                screening.value
                    .sorted()
                    .groupBy { it.toLocalTime() }
                    .map { createScreeningRow(it) }
                    .reduce { r1, r2 -> "$r1\n$r2" },
                false
            )
        }
        return builder.build()
    }

    private fun createDescriptionText(): String {
        val pattern = DateTimeFormatter.ofPattern("YYYY. MMMM d.")
        val from = LocalDate.now().format(pattern)
        val to = LocalDate.now().plusDays(7).format(pattern)
        return "$from - $to"

    }

    private fun createScreeningRow(timeToDateTime: Map.Entry<LocalTime, List<LocalDateTime>>): String {
        val timeString = timeToDateTime.key.format(DateTimeFormatter.ofPattern("H:mm"))
        val dateString = timeToDateTime.value.map {
            it.format(DateTimeFormatter.ofPattern("d."))
        }.reduce { s1, s2 -> "$s1, $s2" }
        return "**$timeString** - $dateString"
    }

}