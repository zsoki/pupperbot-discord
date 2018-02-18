package hu.suppoze.pupperbot.giphy

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.di.kodein
import org.kodein.generic.instance

@ChatCommand(type = AvailableCommands.GIPHY_RANDOM)
class GiphyRandomCommand : UseCase() {

    private val giphyServer: GiphyServer by kodein.instance()

    override fun onExecute() {
        val tag = commandContext.rawArgs ?: throw IllegalStateException("Tag was null.")
        val url = giphyServer.getRandomGiphyBy(tag)

        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}