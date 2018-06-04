package hu.suppoze.pupperbot.app.help

import hu.suppoze.pupperbot.app.common.AvailableCommands
import hu.suppoze.pupperbot.app.common.ChatCommand
import hu.suppoze.pupperbot.app.common.UseCase

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase() {

    override fun onExecute() {
        var helpString = ""
        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        commandContext.event.textChannel.sendMessage(helpString).queue()
    }

}