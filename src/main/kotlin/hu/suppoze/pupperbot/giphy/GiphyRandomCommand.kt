package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase<String> {

    companion object : KLogging()

    private val giphyServer: GiphyServer by kodein.instance()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.message.channel.sendMessage("${parameterizedCommand.paramString} $it")
    }

    override val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
        parameterizedCommand.event.message.channel.sendMessage("Error during giphy request: ${it.message}")
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
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