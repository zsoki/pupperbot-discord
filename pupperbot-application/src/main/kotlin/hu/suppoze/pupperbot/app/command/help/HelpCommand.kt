package hu.suppoze.pupperbot.app.command.help

import hu.suppoze.pupperbot.app.PupperBotApplication
import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase() {

    override suspend fun onExecute() {
        var helpString = "To invite PupperBot to your server, use this link: <${PupperBotApplication.inviteUrl}>\n\n"

        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        commandContext.event.textChannel.sendMessage(helpString).queue()
    }

}