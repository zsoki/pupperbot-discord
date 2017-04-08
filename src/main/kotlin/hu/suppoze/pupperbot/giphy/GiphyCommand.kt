package hu.suppoze.pupperbot.giphy

import hu.suppoze.pupperbot.common.Command
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class GiphyCommand(val event: MessageReceivedEvent, val tag: String) : Command<String> {

    override val onNext: (String) -> Unit = {
        event.message.reply(it)
    }

    override val onError: (Throwable) -> Unit = {
        event.message.channel.sendMessage("Error during giphy request!")
    }

    override fun perform() {
        GiphyServer().requestRandomGiphyUrlByTag(tag)
                .subscribe(onNext, onError)
    }
}