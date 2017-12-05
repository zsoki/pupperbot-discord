package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase() {

    private val giphyServer: GiphyServer by kodein.instance()

    override fun onExecute() {
        val tag = parameterizedCommand.paramString ?: throw IllegalStateException("Tag was null.")

        giphyServer.getRandomGiphyBy(tag)
                .subscribe({
                    parameterizedCommand.event.textChannel.sendMessage("${parameterizedCommand.paramString} $it").queue()
                }, {

                })
    }
}