package hu.suppoze.pupperbot.cinema

import java.time.LocalDateTime

data class Movie(val title: String,
                 val type: String,
                 val language: String,
                 val length: String)

data class Schedule(val locationId: Int,
                    val venueTypeId: Int,
                    val screenings: MutableMap<Movie, MutableList<LocalDateTime>>)
