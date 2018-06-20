package hu.suppoze.pupperbot.app.spawnalert

import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.spawnalert.domain.SubscriptionEvent
import org.kodein.di.generic.instance

@ChatCommand(type = AvailableCommands.SPAWN_ALERT)
class SpawnAlertCommand : UseCase() {

    private val spawnAlertScheduler: SpawnAlertScheduler by kodein.instance()

    override fun onExecute() {
        val channel = commandContext.event.channel
        val subscriptionEvent = spawnAlertScheduler.subscribeChannel(channel)

        channel.sendMessage(
            when (subscriptionEvent) {
                SubscriptionEvent.Subscribed -> "Successfully subscribed channel ${channel.name} to the alert list."
                SubscriptionEvent.Unsubscribed -> "Unsubscribed channel ${channel.name} from the alert list."
            }
        ).queue()
    }

}