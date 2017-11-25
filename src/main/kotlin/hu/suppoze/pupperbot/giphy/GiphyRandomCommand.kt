package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase2<String> {

    private val giphyServer: GiphyServer by kodein.instance()

    val onNext: (String) -> Unit = {
        parameterizedCommand.event.textChannel.sendMessage("${parameterizedCommand.paramString} $it").queue()
    }

    fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        val tag = parameterizedCommand.paramString
        if (tag == null) {
            onError(Throwable("Tag was null."))
            return
        }

        giphyServer.getRandomGiphyBy(tag)
                .subscribe(onNext, onError)
    }
}