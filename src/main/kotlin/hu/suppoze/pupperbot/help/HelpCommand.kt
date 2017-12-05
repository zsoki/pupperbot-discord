package hu.suppoze.pupperbot.help

import hu.suppoze.pupperbot.common.*

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase() {

    override fun onExecute() {
        var helpString = ""
        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        parameterizedCommand.event.textChannel.sendMessage(helpString).queue()
    }

}