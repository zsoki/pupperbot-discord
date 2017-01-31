package giphy

import common.Command
import sx.blah.discord.handle.impl.events.MessageReceivedEvent

class GiphyCommand(val event: MessageReceivedEvent, val tag: String) : Command {

    private val onSuccess: (String) -> Unit = {
        event.message.reply(it)
    }

    private val onFailure: () -> Unit = {
        event.message.channel.sendMessage("Error during giphy request, bork :(")
    }

    override fun perform() {
        GiphyServer().requestRandomGiphyUrlByTag(tag, onSuccess, onFailure)
    }
}