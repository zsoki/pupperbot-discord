package hu.suppoze.pupperbot.common

import hu.suppoze.pupperbot.common.CommandFactory.Commands.GIPHY
import hu.suppoze.pupperbot.common.CommandFactory.Commands.HELP
import hu.suppoze.pupperbot.common.CommandFactory.Commands.RSS
import hu.suppoze.pupperbot.giphy.GiphyCommand
import hu.suppoze.pupperbot.help.HelpCommand
import hu.suppoze.pupperbot.rss.RssCommand
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandFactory(val event: MessageReceivedEvent) {

    val commandLine: String = event.message.content.trimStart(';')

    fun  build(): Command<*> {
        val words = commandLine.split(' ')
        return determineAndCreateCommand(words[0], words.drop(1))
    }

    private fun determineAndCreateCommand(command: String, params: List<String>): Command<*> {
        when (command) {
            RSS -> return RssCommand(event)
            GIPHY -> return GiphyCommand(event, params.reduce { s1, s2 -> "$s1+$s2" })
            HELP -> return HelpCommand(event)
            else -> return CommandError(event)
        }
    }

    internal object Commands {
        const val RSS: String = "rss"
        const val GIPHY: String = "giphy"
        const val HELP: String = "help"
    }
}