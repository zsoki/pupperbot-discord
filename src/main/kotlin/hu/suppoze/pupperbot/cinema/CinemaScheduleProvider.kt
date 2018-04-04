package hu.suppoze.pupperbot.cinema

interface CinemaScheduleProvider {

    fun fetchNextWeekSchedule(city: String): Schedule

}