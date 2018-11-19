package hu.suppoze.pupperbot.app.command.cinema.domain

interface CinemaScheduleProvider {

    fun fetchNextWeekSchedule(city: String): Schedule

}