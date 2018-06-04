package hu.suppoze.pupperbot.app.cinema.api

import com.google.gson.Gson
import hu.suppoze.pupperbot.app.common.http.RestClient
import hu.suppoze.pupperbot.app.di.kodein
import org.kodein.di.generic.instance
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CinemaApiClientImpl : CinemaApiClient {

    private val cinemaListRequestUrl: String by kodein.instance("cinemaListRequestUrl")
    private val cinemaFilmEventsRequestUrl: String by kodein.instance("cinemaFilmEventsRequestUrl")
    private val client: RestClient by kodein.instance()
    private val gson: Gson by kodein.instance()

    override fun getCinemas(): List<CinemaApiCinema> {
        val responseBody = client.get(
            cinemaListRequestUrl,
            listOf("attr" to "", "lang" to "hu_HU"),
            getUntilIsoDate()
        )

        val cinemaApiResponse = gson.fromJson(responseBody, CinemaApiCinemasResponse::class.java)
        return cinemaApiResponse.body.cinemas
    }

    override fun getFilmEventsFor(cinemaId: String, date: LocalDate): CinemaApiFilmEvents {
        val responseBody = client.get(
            cinemaFilmEventsRequestUrl,
            listOf("attr" to "", "lang" to "hu_HU"),
            cinemaId, date.format(DateTimeFormatter.ISO_DATE)
        )

        val cinemaApiResponse = gson.fromJson(responseBody, CinemaApiFilmEventsResponse::class.java)
        return cinemaApiResponse.body
    }

    private fun getUntilIsoDate(): String = LocalDate.now()
        .plusDays(7L)
        .format(DateTimeFormatter.ISO_DATE)

}