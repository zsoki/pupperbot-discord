package hu.suppoze.pupperbot.cinema.domain

interface CinemaScheduleProvider {

    fun fetchNextWeekSchedule(city: String): Schedule

}