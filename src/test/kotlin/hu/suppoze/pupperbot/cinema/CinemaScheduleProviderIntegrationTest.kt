package hu.suppoze.pupperbot.cinema

import hu.suppoze.pupperbot.cinema.domain.CinemaScheduleProviderImpl
import hu.suppoze.pupperbot.cinema.domain.Schedule
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("integration")
internal class CinemaScheduleProviderIntegrationTest {

    private val cinemaScheduleProvider = CinemaScheduleProviderImpl()

    private lateinit var city: String
    private lateinit var schedule: Schedule

    @Test
    fun fetchNextWeekSchedule() {
        givenCity("szeged")
        whenNextWeekScheduleIsRequested()
        thenScheduleShouldNotBeEmpty()
    }

    private fun givenCity(city: String) {
        this.city = city
    }

    private fun whenNextWeekScheduleIsRequested() {
        schedule = cinemaScheduleProvider.fetchNextWeekSchedule(city)
    }

    private fun thenScheduleShouldNotBeEmpty() {
        assertTrue(!schedule.cinemaName.isEmpty())
        assertTrue(!schedule.screenings.isEmpty())
    }

}