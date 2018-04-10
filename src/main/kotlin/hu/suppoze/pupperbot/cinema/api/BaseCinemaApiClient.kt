package hu.suppoze.pupperbot.cinema.api

import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class BaseCinemaApiClient : CinemaApiClient {

    protected fun getUntilIsoDate(): String = LocalDate.now()
        .plusDays(7L)
        .format(DateTimeFormatter.ISO_DATE)

    object ApiUrl {
        const val CINEMA_LIST_REQUEST_URL =
            "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/{{0}}"
        const val CINEMA_FILM_EVENTS_REQUEST_URL =
            "http://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/film-events/in-cinema/{{0}}/at-date/{{1}}"
    }

}