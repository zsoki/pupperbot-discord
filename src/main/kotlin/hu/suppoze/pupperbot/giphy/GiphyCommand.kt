package hu.suppoze.pupperbot.giphy

import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser

class GiphyCommand(val rawCommand: CommandParser.RawCommand) : UseCase<String> {

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.channel.sendMessage("${rawCommand.rawParams}: $it")
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during giphy request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute() {
        val tag = rawCommand.rawParams

        if (tag == null) {
            onError(Throwable("Tag was null."))
            return
        }

        GiphyServer().requestRandomGiphyUrlByTag(tag)
                .subscribe(onNext, onError)
    }
}