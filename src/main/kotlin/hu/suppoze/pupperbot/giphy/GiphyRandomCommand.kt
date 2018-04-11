package hu.suppoze.pupperbot.giphy

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.giphy.api.GiphyClientImpl
import org.kodein.di.generic.instance

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase() {

    private val giphyServer: GiphyClientImpl by kodein.instance()

    override fun onExecute() {
        val tag = commandContext.rawArgs ?: throw IllegalStateException("Tag was null.")
        val url = giphyServer.getRandomGiphyBy(tag)

        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}