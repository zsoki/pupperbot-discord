package hu.suppoze.pupperbot.app.help

import hu.suppoze.pupperbot.app.PupperBotApplication
import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase() {

    override fun onExecute() {
        var helpString = "To invite PupperBot to your server, use this link: <${PupperBotApplication.inviteUrl}>\n\n"

        for (item in AvailableCommands.values()) {
            helpString += "${item.example} - ${item.description}\n"
        }

        commandContext.event.textChannel.sendMessage(helpString).queue()
    }

}