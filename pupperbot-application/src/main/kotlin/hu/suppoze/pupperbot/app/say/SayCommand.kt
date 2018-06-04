package hu.suppoze.pupperbot.app.say

import hu.suppoze.pupperbot.app.common.AvailableCommands
import hu.suppoze.pupperbot.app.common.ChatCommand
import hu.suppoze.pupperbot.app.common.UseCase

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