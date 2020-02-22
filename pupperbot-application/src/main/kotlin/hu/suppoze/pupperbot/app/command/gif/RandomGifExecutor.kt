package hu.suppoze.pupperbot.app.command.gif

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.common.CommandExecutor
import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient

class RandomGifExecutor(private val gifClient: PupperGifClient) : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
        val tag = commandContext.rawArgs ?: throw IllegalStateException("Tag was null.")
        val url = gifClient.getRandomGifBy(tag)

//        commandContext.event.sendMessage("${commandContext.rawArgs} $url").queue() TODO
    }
}