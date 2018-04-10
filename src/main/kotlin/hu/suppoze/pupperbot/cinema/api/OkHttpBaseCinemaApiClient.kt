package hu.suppoze.pupperbot.cinema.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hu.suppoze.pupperbot.common.http.RestClient
import hu.suppoze.pupperbot.di.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class OkHttpBaseCinemaApiClient : BaseCinemaApiClient() {

    private val client: RestClient by kodein.instance()
    private val gson: Gson = GsonBuilder().create() // TODO: dependency injection

    override fun getCinemas(): List<CinemaApiCinema> {
        val response = client.get(
            ApiUrl.CINEMA_LIST_REQUEST_URL,
            listOf("attr" to "", "lang" to "hu_HU"),
            getUntilIsoDate()
        )

        val cinemaApiResponse = gson.fromJson(response, CinemaApiCinemasResponse::class.java)

        return cinemaApiResponse.body.cinemas
    }

    override fun getFilmEventsFor(cinemaId: String, date: LocalDate): CinemaApiFilmEvents {
        val response = client.get(
            ApiUrl.CINEMA_FILM_EVENTS_REQUEST_URL,
            listOf("attr" to "", "lang" to "hu_HU"),
            cinemaId, date.format(DateTimeFormatter.ISO_DATE)
        )

        val cinemaApiResponse = gson.fromJson(response, CinemaApiFilmEventsResponse::class.java)

        return cinemaApiResponse.body
    }

}