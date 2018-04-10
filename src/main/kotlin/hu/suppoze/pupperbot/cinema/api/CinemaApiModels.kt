package hu.suppoze.pupperbot.cinema.api

data class CinemaApiCinemasResponse(
    val body: CinemaApiCinemasWrapper
)

data class CinemaApiCinemasWrapper(
    val cinemas: List<CinemaApiCinema>
)

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

data class CinemaApiFilmEventsResponse(
    val body: CinemaApiFilmEvents
)

data class CinemaApiFilmEvents(
    val films: List<CinemaApiFilm>,
    val events: List<CinemaApiEvent>
)

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