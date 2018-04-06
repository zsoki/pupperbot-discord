package hu.suppoze.pupperbot.cinema.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import hu.suppoze.pupperbot.common.http.RestClient
import hu.suppoze.pupperbot.util.withPathParams
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO use RestClient when it's done to remove dependency on third party http lib
class CinemaApiClient : RestClient {

    private val cinemaListRequestUrl =
        "https://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/cinemas/with-event/until/{{0}}"
    private val cinemaFilmEventsRequestUrl =
        "https://www.cinemacity.hu/hu/data-api-service/v1/quickbook/10102/film-events/in-cinema/{{0}}/at-date/{{1}}"

    override fun buildGetRequest(urlParams: List<Pair<String, Any>>?, pathParams: List<Pair<String, Any>>?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getCinemas(): List<CinemaApiCinema> {
        val isoDate = LocalDate.now()
            .plusDays(7L)
            .format(DateTimeFormatter.ISO_DATE)

        val (_, _, result) =
                Fuel.get(cinemaListRequestUrl.withPathParams(isoDate), listOf("lang" to "hu_HU"))
                    .timeout(3000)
                    .responseObject<CinemaApiCinemasWrapper>()
        val (cinemasWrapper, fuelError) = result

        if (cinemasWrapper == null)
            throw fuelError!!.exception

        return cinemasWrapper.cinemas
    }

    fun getFilmEventsFor(cinemaId: String, date: LocalDate): CinemaApiFilmEvents {
        val isoDate = date
            .format(DateTimeFormatter.ISO_DATE)

        val (_, _, result) =
                Fuel.get(cinemaListRequestUrl.withPathParams(cinemaId.toString(), isoDate), listOf("lang" to "hu_HU"))
                    .timeout(3000)
                    .responseObject<CinemaApiFilmEvents>()
        val (cinemaApiFilmEvents, fuelError) = result

        if (cinemaApiFilmEvents == null)
            throw fuelError!!.exception

        return cinemaApiFilmEvents
    }

}