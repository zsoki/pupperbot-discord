package hu.suppoze.pupperbot.common

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

class CommandParser {

    fun isValidCommand(rawContent: String): Boolean {
        val appendedCommands = CommandProvider
                .getCommandStrings()
                .reduce {first, second -> first + "|" + second }
        return rawContent.matches(Regex("^;($appendedCommands).*"))
    }

    fun createParameterizedCommand(event: MessageReceivedEvent): ParameterizedCommand {
        val commandLine: String = event.message.rawContent.trimStart(';')
        val words = commandLine.split(' ')
        return ParameterizedCommand(
                event,
                words[0],
                extractRawParams(commandLine, words[0]),
                extractParams(words)
        )
    }

    private fun extractRawParams(commandLine: String, commandString: String) =
            commandLine.drop(commandString.length + 1)

    private fun extractParams(words: List<String>) = if (words.size > 1) words.drop(1) else null

}
