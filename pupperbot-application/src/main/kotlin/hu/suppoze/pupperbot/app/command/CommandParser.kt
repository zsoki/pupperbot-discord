package hu.suppoze.pupperbot.app.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandParser {

    fun isValidCommand(rawContent: String): Boolean {
        val appendedCommands = CommandProvider.getCommandStrings()
            .reduce { first, second -> "$first|$second" }
        return rawContent.matches(Regex("^;($appendedCommands).*"))
    }

    fun buildCommandContext(event: MessageReceivedEvent): CommandContext {
        val commandLine: String = event.message.contentRaw.trimStart(';')
        val words = commandLine.split(' ')
        return CommandContext(
            event,
            words[0],
            extractRawArguments(commandLine, words[0]),
            extractArgumentList(words)
        )
    }

    private fun extractRawArguments(commandLine: String, commandString: String) =
        commandLine.drop(commandString.length + 1)

    private fun extractArgumentList(words: List<String>) = if (words.size > 1) words.drop(1) else null

}
