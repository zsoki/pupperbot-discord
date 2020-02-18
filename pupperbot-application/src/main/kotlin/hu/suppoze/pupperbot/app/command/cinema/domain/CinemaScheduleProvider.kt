package hu.suppoze.pupperbot.app.command.cinema.domain

interface CinemaScheduleProvider {

    // TODO: suspend?
    fun fetchNextWeekSchedule(city: String): Schedule

}