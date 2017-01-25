import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.ReadyEvent

class AnnotationListener {

    @EventSubscriber
    fun onReadyEvent(event: ReadyEvent) {
        Main.listenForCommand()
    }

    @EventSubscriber
    fun onMessageReceiedEvent(event: MessageReceivedEvent) {
        if (event.message.content == "Hi, PupperBot!") {
            event.message.reply("Bork!")
        }
    }

}
