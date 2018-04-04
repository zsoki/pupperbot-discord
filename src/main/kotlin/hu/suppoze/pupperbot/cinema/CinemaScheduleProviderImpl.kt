package hu.suppoze.pupperbot.cinema

import hu.suppoze.pupperbot.cinema.api.CinemaApi
import hu.suppoze.pupperbot.di.kodein
import org.kodein.di.generic.instance

class CinemaScheduleProviderImpl : CinemaScheduleProvider {

    private val cinemaApi: CinemaApi by kodein.instance()

    override fun fetchNextWeekSchedule(city: String): Schedule {
        TODO("reimplement")
    }

}