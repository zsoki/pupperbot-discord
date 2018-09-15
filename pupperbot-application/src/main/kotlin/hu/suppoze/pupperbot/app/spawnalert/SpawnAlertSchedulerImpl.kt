package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.spawnalert.domain.Alert
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnAlertSchedule
import hu.suppoze.pupperbot.app.spawnalert.domain.SpawnEntry
import hu.suppoze.pupperbot.app.spawnalert.domain.SubscriptionEvent
import kotlinx.coroutines.*
import net.dv8tion.jda.core.entities.MessageChannel
import org.kodein.di.generic.instance
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class SpawnAlertSchedulerImpl : SpawnAlertScheduler {

    private val spawnAlertImporter: SpawnAlertImporter by kodein.instance()

    private val subscribedChannels = mutableSetOf<MessageChannel>()
    private var schedule: SpawnAlertSchedule? = null

    override fun start() {
        schedule = spawnAlertImporter.importSchedule()

        with(schedule!!) {
            GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
                while (true) {
                    delay(10, TimeUnit.SECONDS)
                    val time = LocalDateTime.now()
                    spawnEntries.forEach { alertWithinThreshold(time, it, true) }
                }
            })
        }
    }

    private fun alertWithinThreshold(dateTime: LocalDateTime, spawnEntry: SpawnEntry, scheduledAlert: Boolean) {
        val dayOfWeek = dateTime.dayOfWeek
        if (dayOfWeek != spawnEntry.dayOfWeek) return

        val duration = Duration.between(dateTime.toLocalTime(), spawnEntry.time).toMinutes()
        if (duration <= 0) return

        val alertsAscending = spawnEntry.alertStates
            .filter { duration <= it.minuteThreshold && (it.shouldAlert || !scheduledAlert) }
            .sortedBy { it.minuteThreshold }

        val soonestAlert = alertsAscending.firstOrNull() ?: return

        alertChannels(spawnEntry, soonestAlert)

        if (!scheduledAlert) return

        alertsAscending.forEach { it.shouldAlert = false }
    }

    private fun alertChannels(spawnEntry: SpawnEntry, alert: Alert) {
        subscribedChannels.forEach {
            it.sendMessage("@here Boss Spawn Alert: ${spawnEntry.boss} spawns in ${alert.minuteThreshold} minutes (${spawnEntry.time})")
                .queue()
        }
    }

    override fun subscribeChannel(channel: MessageChannel?): SubscriptionEvent {
        if (channel == null) throw RuntimeException("Channel is null.")

        if (subscribedChannels.contains(channel)) {
            subscribedChannels.remove(channel)
            return SubscriptionEvent.Unsubscribed
        }

        subscribedChannels.add(channel)
        schedule?.spawnEntries?.forEach { alertWithinThreshold(LocalDateTime.now(), it, false) }
        return SubscriptionEvent.Subscribed
    }

}
