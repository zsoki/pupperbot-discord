package hu.suppoze.pupperbot.app.cinema.api

import java.time.LocalDate

interface CinemaApiClient {

    fun getCinemas(): List<CinemaApiCinema>

    fun getFilmEventsFor(cinemaId: String, date: LocalDate): CinemaApiFilmEvents

}