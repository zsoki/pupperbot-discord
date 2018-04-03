package hu.suppoze.pupperbot.cinema

interface CinemaScheduleProvider {

    fun fetchSchedule(): Schedule

}