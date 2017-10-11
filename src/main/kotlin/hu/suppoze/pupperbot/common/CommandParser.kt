package hu.suppoze.pupperbot.common

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class CommandParser {

    fun parse(event: MessageReceivedEvent): ParameterizedCommand {
        val commandLine: String = event.message.content.trimStart(';')
        val words = commandLine.split(' ')
        return ParameterizedCommand(
                event,
                words[0],
                commandLine.drop(words[0].length + 1),
                if (words.size > 1) words.drop(1) else null
        )
    }
}
