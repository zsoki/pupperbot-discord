package hu.suppoze.pupperbot.cinema.api

interface CinemaApiBody

data class CinemaApiResponse(
    val body: CinemaApiBody
)

data class CinemaApiFilmEvents(
    val films: List<CinemaApiFilm>,
    val events: List<CinemaApiEvent>
) : CinemaApiBody

data class CinemaApiFilm(
    val id: String,
    val name: String,
    val length: Int,
    val posterLink: String,
    val videoLink: String,
    val link: String,
    val weight: Double,
    val releaseYear: String,
    val attributeIds: List<String>
)

data class CinemaApiEvent(
    val id: String,
    val filmId: String,
    val cinemaId: String,
    val businessDay: String,
    val eventDateTime: String,
    val attributeIds: List<String>,
    val bookingLink: String,
    val soldOut: Boolean
)

data class CinemaApiCinemasWrapper(
    val cinemas: List<CinemaApiCinema>
) : CinemaApiBody

data class CinemaApiCinema(
    val id: String,
    val groupId: String,
    val displayName: String,
    val link: String,
    val address: String,
    val bookingUrl: String,
    val blockOnlineSales: Boolean,
    val blockOnlineSalesUntil: String
)
