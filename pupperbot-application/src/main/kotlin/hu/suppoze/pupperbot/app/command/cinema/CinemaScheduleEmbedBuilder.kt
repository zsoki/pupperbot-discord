package hu.suppoze.pupperbot.app.command.cinema

import hu.suppoze.pupperbot.app.command.cinema.domain.Movie
import hu.suppoze.pupperbot.app.command.cinema.domain.Screening
import hu.suppoze.pupperbot.app.di.kodein
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.kodein.di.generic.instance
import java.awt.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// TODO: dependency inversion
class CinemaScheduleEmbedBuilder {

    private val cinemaCityLogoUrl: String by kodein.instance("cinemaCityLogoUrl")

    fun buildMovieListEmbed(cinemaName: String, emoteToScreening: Map<String, Screening>): MessageEmbed {
        val builder = EmbedBuilder()
            .setTitle("$cinemaName vetítések")
            .setDescription(createMovieListDescriptionText())
            .setColor(Color.ORANGE)
            .setThumbnail(cinemaCityLogoUrl)

        emoteToScreening.forEach {
            builder.addField(
                "${it.key} -> ${it.value.movie.title}",
                createMovieAttributesDescriptionText(it.value.movie),
                true
            )
        }

        return builder.build()
    }

    fun buildScreeningTimeEmbed(screening: Screening): MessageEmbed {
        val builder = EmbedBuilder()
            .setTitle("${screening.movie.title} időpontok")
            .setDescription(createMovieAttributesDescriptionText(screening.movie))
            .setColor(Color.ORANGE)

        screening.screeningTimes.groupBy { it.toLocalDate() }
            .forEach {
                builder.addField(
                    it.key.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                    it.value.map { time -> time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) }
                        .reduce { s1, s2 -> "$s1\n$s2" },
                    true
                )
            }

        return builder.build()
    }

    private fun createMovieListDescriptionText(): String {
        val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
        val from = LocalDate.now().format(pattern)
        val to = LocalDate.now().plusDays(7).format(pattern)
        return "$from - $to"

    }

    private fun createMovieAttributesDescriptionText(movie: Movie) = movie.attributes.reduce { r1, r2 -> "$r1 | $r2" }

}