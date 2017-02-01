package giphy

import common.Command
import sx.blah.discord.handle.impl.events.MessageReceivedEvent

class GiphyCommand(val event: MessageReceivedEvent, val tag: String) : Command {

    private val onNext: (String) -> Unit = {
        event.message.reply(it)
    }

    private val  onError: ((t: Throwable) -> Unit)? = {
        event.message.channel.sendMessage("Error during giphy request!")
    }

    override fun perform() {
        GiphyServer().requestRandomGiphyUrlByTag(tag)
                .subscribe(onNext, onError)
    }
}