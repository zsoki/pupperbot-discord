package hu.suppoze.pupperbot.app.command.cinema.domain

import java.time.LocalDateTime

data class Schedule(
    val cinemaName: String,
    val screenings: List<Screening>
)

data class Screening(
    val movie: Movie,
    val screeningTimes: List<LocalDateTime>
)

data class Movie(
    val title: String,
    val length: Int,
    val attributes: List<String>
)
