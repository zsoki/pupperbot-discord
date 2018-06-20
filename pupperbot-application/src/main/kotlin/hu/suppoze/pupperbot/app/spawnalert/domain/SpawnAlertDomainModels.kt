package hu.suppoze.pupperbot.app.spawnalert.domain

import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZoneOffset

data class SpawnAlertSchedule(
    val spawnEntries: List<SpawnEntry>
)

data class SpawnEntry(
    val dayOfWeek: DayOfWeek,
    val time: LocalTime,
    val boss: String,
    val offset: ZoneOffset = defaultOffset,
    val alertStates: List<Alert> = listOf(Alert(10), Alert(30), Alert(60), Alert(120))
) {
    companion object {
        val defaultOffset: ZoneOffset = ZoneOffset.ofHours(2) // CEST = Europe/Budapest
    }
}

data class Alert(
    val minuteThreshold: Long,
    var shouldAlert: Boolean = true
)

enum class SubscriptionEvent {
    Subscribed, Unsubscribed
}