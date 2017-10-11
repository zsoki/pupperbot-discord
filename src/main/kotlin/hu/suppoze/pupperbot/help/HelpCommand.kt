package hu.suppoze.pupperbot.help

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase
import mu.KLogging

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase<String> {

    companion object : KLogging()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.textChannel.sendMessage(it).queue()
    }

    override val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        var helpString = ""
        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        onNext(helpString)
    }
}