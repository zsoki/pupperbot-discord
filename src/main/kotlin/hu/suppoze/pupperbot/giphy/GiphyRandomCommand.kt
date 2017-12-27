package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase() {

    private val giphyServer: GiphyServer by kodein.instance()

    override fun onExecute() {
        val tag = commandContext.rawArgs ?: throw IllegalStateException("Tag was null.")
        val url = giphyServer.getRandomGiphyBy(tag)

        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}