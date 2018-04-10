package hu.suppoze.pupperbot.cinema.domain

import hu.suppoze.pupperbot.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.cinema.api.CinemaApiFilm
import hu.suppoze.pupperbot.cinema.api.CinemaApiFilmEvents
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.util.containsAsciiPrintableIgnoreCase
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.LocalDateTime

class CinemaScheduleProviderImpl : CinemaScheduleProvider {

    private val cinemaApi: CinemaApiClient by kodein.instance()

    override fun fetchNextWeekSchedule(city: String): Schedule {
        val cinema = cinemaApi.getCinemas()
            .single { it.displayName.containsAsciiPrintableIgnoreCase(city) }
        val filmEvents = cinemaApi.getFilmEventsFor(cinema.id, LocalDate.now().plusDays(1L)) // TODO all week
        val screeningsByMovie = filmEvents.films.associate { collectScreeningsByMovie(it, filmEvents) }
        return Schedule(cinema.displayName, screeningsByMovie)
    }

    private fun collectScreeningsByMovie(
        film: CinemaApiFilm,
        filmEvents: CinemaApiFilmEvents
    ) = Pair(
        Movie(film.name, film.length, film.attributeIds),
        filmEvents.events.filter { event -> event.filmId == film.id }
            .map { LocalDateTime.parse(it.eventDateTime) }
    )

}