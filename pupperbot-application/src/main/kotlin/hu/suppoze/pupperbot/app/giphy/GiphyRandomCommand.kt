package hu.suppoze.pupperbot.app.giphy

import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.giphy.api.GiphyClient
import org.kodein.di.generic.instance

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase() {

    private val giphyServer: GiphyClient by kodein.instance()

    override fun onExecute() {
        val tag = commandContext.rawArgs ?: throw IllegalStateException("Tag was null.")
        val url = giphyServer.getRandomGiphyBy(tag)

        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}