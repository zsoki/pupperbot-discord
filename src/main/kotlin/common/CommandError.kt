package common

import sx.blah.discord.handle.impl.events.MessageReceivedEvent

class CommandError(val event: MessageReceivedEvent) : Command {
    override fun perform() {
        event.message.channel.sendMessage("Bork? :|")
    }
}