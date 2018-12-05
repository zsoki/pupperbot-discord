package hu.suppoze.pupperbot.app.command.cinema.domain

import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiCinema
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiFilm
import hu.suppoze.pupperbot.app.command.cinema.api.CinemaApiFilmEvents
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.util.containsAsciiPrintableIgnoreCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.LocalDateTime

class CinemaScheduleProviderImpl : CinemaScheduleProvider {

    private val cinemaClient: CinemaApiClient by kodein.instance()

    override suspend fun fetchNextWeekSchedule(city: String): Schedule {
        return coroutineScope {
            val cinema = findCinema(city)
            val nextWeekScreenings = HashMap<Movie, MutableList<LocalDateTime>>()

            (0..7).map { LocalDate.now().plusDays(it.toLong()) }
                .map { async { getMoviesForDay(cinema.id, it) } }
                .forEach { mergeScreenings(it.await(), nextWeekScreenings) }

            val screenings = nextWeekScreenings.map { Screening(it.key, it.value) }
            Schedule(cinema.displayName, screenings)
        }
    }

    private fun findCinema(city: String): CinemaApiCinema =
        cinemaClient.getCinemas()
            .single { it.displayName.containsAsciiPrintableIgnoreCase(city) }

    private fun getMoviesForDay(cinemaId: String, date: LocalDate): Map<Movie, List<LocalDateTime>> {
        val filmEvents = cinemaClient.getFilmEventsFor(cinemaId, date)
        return filmEvents.films.associate { collectScreeningsByMovie(it, filmEvents) }
    }

    private fun collectScreeningsByMovie(
        film: CinemaApiFilm,
        filmEvents: CinemaApiFilmEvents
    ) = Pair(
        Movie(film.name, film.length, film.attributeIds),
        filmEvents.events.asSequence()
            .filter { event -> event.filmId == film.id }
            .map { LocalDateTime.parse(it.eventDateTime) }.toList()
    )

    private fun mergeScreenings(
        singleDayScreenings: Map<Movie, List<LocalDateTime>>,
        weeklyScreenings: HashMap<Movie, MutableList<LocalDateTime>>
    ) {
        singleDayScreenings.forEach {
            if (!weeklyScreenings.containsKey(it.key)) {
                weeklyScreenings[it.key] = it.value.toMutableList()
            } else {
                weeklyScreenings[it.key]!!.addAll(it.value)
            }
        }
    }
}
