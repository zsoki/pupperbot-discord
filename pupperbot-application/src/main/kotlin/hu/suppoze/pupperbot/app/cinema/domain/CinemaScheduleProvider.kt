package hu.suppoze.pupperbot.app.cinema.domain

interface CinemaScheduleProvider {

    fun fetchNextWeekSchedule(city: String): Schedule

}