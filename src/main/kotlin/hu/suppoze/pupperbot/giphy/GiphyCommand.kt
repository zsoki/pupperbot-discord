package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.di.kodein

class GiphyCommand(val rawCommand: CommandParser.RawCommand) : UseCase<String> {

    private val giphyServer: GiphyServer by kodein.instance()

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.channel.sendMessage("${rawCommand.rawParams} $it")
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Error during giphy request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute() {
        val tag = rawCommand.rawParams

        if (tag == null) {
            onError(Throwable("Tag was null."))
            return
        }

        giphyServer.requestRandomGiphyUrlByTag(tag)
                .subscribe(onNext, onError)
    }
}