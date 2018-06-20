package hu.suppoze.pupperbot.app.say

import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase() {

    override fun onExecute() {
        if (commandContext.rawArgs == null || commandContext.rawArgs!!.isEmpty()) {
            throw IllegalArgumentException("Argument string is null or empty")
        }

        commandContext.event.message.delete().queue()
        commandContext.event.textChannel.sendMessage(commandContext.rawArgs).queue()
    }
}