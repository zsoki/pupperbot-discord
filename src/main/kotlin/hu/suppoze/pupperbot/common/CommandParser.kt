package hu.suppoze.pupperbot.common

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandParser {

    fun parse(event: MessageReceivedEvent): RawCommand {
        val commandLine: String = event.message.content.trimStart(';')
        val words = commandLine.split(' ')
        return RawCommand(
                event,
                words[0],
                commandLine.drop(words[0].length + 1),
                if (words.size > 1) words.drop(1) else null
        )
    }
}
