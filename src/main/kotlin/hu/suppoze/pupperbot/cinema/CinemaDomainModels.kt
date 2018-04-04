package hu.suppoze.pupperbot.cinema

import java.time.LocalDateTime

data class Schedule(
    val cinemaName: String,
    val screenings: Map<Movie, LocalDateTime>
)

data class Movie(
    val title: String,
    val length: Int,
    val attributes: List<String>
)
