package hu.suppoze.pupperbot.app.command.cinema.domain

interface CinemaScheduleProvider {

    suspend fun fetchNextWeekSchedule(city: String): Schedule

}