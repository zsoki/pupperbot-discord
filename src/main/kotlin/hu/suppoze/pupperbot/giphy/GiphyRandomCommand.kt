package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase<String> {

    companion object : KLogging()

    private val giphyServer: GiphyServer by kodein.instance()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.textChannel.sendMessage("${parameterizedCommand.paramString} $it").queue()
    }

    override val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
        parameterizedCommand.event.textChannel.sendMessage("Error during giphy request: ${it.message}").queue()
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