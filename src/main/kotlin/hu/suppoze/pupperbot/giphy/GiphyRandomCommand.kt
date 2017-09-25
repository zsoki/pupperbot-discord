package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase<String> {

    private val giphyServer: GiphyServer by kodein.instance()

    private lateinit var rawCommand: RawCommand

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.channel.sendMessage("${rawCommand.rawParams} $it")
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Error during giphy request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(rawCommand: RawCommand) {
        this.rawCommand = rawCommand

        val tag = rawCommand.rawParams

        if (tag == null) {
            onError(Throwable("Tag was null."))
            return
        }

        giphyServer.getRandomGiphyBy(tag)
                .subscribe(onNext, onError)
    }
}