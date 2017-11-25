package hu.suppoze.pupperbot.help

import hu.suppoze.pupperbot.common.*
import mu.KLogging

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase2<String>() {

    override fun onExecute() {
        this.parameterizedCommand = parameterizedCommand

        var helpString = ""
        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        parameterizedCommand.event.textChannel.sendMessage(helpString).queue()
    }

}