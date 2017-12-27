package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase

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