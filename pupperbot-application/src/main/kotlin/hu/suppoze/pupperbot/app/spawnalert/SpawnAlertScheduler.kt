package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.spawnalert.domain.SubscriptionEvent
import net.dv8tion.jda.core.entities.MessageChannel

interface SpawnAlertScheduler {

    fun start()

    fun subscribeChannel(channel: MessageChannel?): SubscriptionEvent

}