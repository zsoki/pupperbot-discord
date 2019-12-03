package hu.suppoze.pupperbot.app.command.say

import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase() {

    override suspend fun onExecute() {
        if (commandContext.rawArgs == null || commandContext.rawArgs!!.isEmpty()) {
            throw IllegalArgumentException("Argument string is null or empty")
        }

        commandContext.event.message.delete().queue()
        commandContext.event.textChannel.sendMessage(commandContext.rawArgs!!).queue()
    }
}