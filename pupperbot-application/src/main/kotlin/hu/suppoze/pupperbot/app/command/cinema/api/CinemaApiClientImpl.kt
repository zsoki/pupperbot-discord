package hu.suppoze.pupperbot.app.command.cinema.api

import com.google.gson.Gson
import hu.suppoze.pupperbot.app.http.PupperRestClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CinemaApiClientImpl(
    private val clientPupper: PupperRestClient,
    private val cinemaListRequestUrl: String = "cinemaListRequestUrl",
    private val cinemaFilmEventsRequestUrl: String = "cinemaFilmEventsRequestUrl"
) : CinemaApiClient {

    // TODO dependency injection
    private val gson = Gson().newBuilder().create()

    override fun getCinemas(): List<CinemaApiCinema> {
        val responseBody = clientPupper.get(
            cinemaListRequestUrl,
            listOf("attr" to "", "lang" to "hu_HU"),
            getUntilIsoDate()
        )

        val cinemaApiResponse = gson.fromJson(responseBody, CinemaApiCinemasResponse::class.java)
        return cinemaApiResponse.body.cinemas
    }

    override fun getFilmEventsFor(cinemaId: String, date: LocalDate): CinemaApiFilmEvents {
        val responseBody = clientPupper.get(
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