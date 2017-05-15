package hu.suppoze.pupperbot.common

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandParser {

    fun parse(event: MessageReceivedEvent) : RawCommand {
        val commandLine: String = event.message.content.trimStart(';')
        val words = commandLine.split(' ')
        return RawCommand(
                event,
                words[0],
                commandLine.drop(words[0].length + 1),
                if (words.size > 1 ) words.drop(1) else null
        )
    }

    internal object CommandStrings {
        const val RSSSUB: String = "rsssub"
        const val RSSUNSUB: String = "rssunsub"
        const val GIPHY: String = "giphy"
        const val HELP: String = "help"
        const val SAY: String = "say"
    }

    data class RawCommand(
            val event: MessageReceivedEvent,
            val command: String,
            val rawParams: String? = null,
            val parameters: List<String>? = null
    )
}
