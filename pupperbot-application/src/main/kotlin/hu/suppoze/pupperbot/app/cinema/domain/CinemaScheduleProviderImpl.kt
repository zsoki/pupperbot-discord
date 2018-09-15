package hu.suppoze.pupperbot.app.cinema.domain

import hu.suppoze.pupperbot.app.cinema.api.CinemaApiCinema
import hu.suppoze.pupperbot.app.cinema.api.CinemaApiClient
import hu.suppoze.pupperbot.app.cinema.api.CinemaApiFilm
import hu.suppoze.pupperbot.app.cinema.api.CinemaApiFilmEvents
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.util.containsAsciiPrintableIgnoreCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.LocalDateTime

class CinemaScheduleProviderImpl : CinemaScheduleProvider {

    private val cinemaClient: CinemaApiClient by kodein.instance()

    override fun fetchNextWeekSchedule(city: String): Schedule = runBlocking {
        val cinema = findCinema(city)
        val scheduleContext = newSingleThreadContext("ScheduleContext")
        val channel = Channel<Map<Movie, List<LocalDateTime>>>()
        val nextWeekScreenings = HashMap<Movie, MutableList<LocalDateTime>>()

        (0..7).map { LocalDate.now().plusDays(it.toLong()) }
            .forEach { launch(scheduleContext) { channel.send(getMoviesForDay(cinema.id, it)) } }

        (0..7).forEach { _ ->
            val screeningsByMovie = channel.receive()
            mergeScreenings(screeningsByMovie, nextWeekScreenings)
        }

        Schedule(cinema.displayName, nextWeekScreenings)
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
