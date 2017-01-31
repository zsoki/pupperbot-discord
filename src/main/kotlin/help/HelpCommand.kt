package help

import common.Command
import sun.plugin2.message.Message
import sx.blah.discord.handle.impl.events.MessageReceivedEvent

class HelpCommand(val event: MessageReceivedEvent) : Command {
    override fun perform() {
        event.message.channel.sendMessage(";giphy <tag> - random gif")
    }
}