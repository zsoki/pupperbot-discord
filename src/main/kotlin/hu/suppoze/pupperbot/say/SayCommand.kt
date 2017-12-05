package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase() {

    override fun onExecute() {
        if (parameterizedCommand.paramString == null || parameterizedCommand.paramString!!.isEmpty()) {
            throw IllegalArgumentException("Parameter string is null or empty")
        }

        parameterizedCommand.event.message.delete().queue()
        parameterizedCommand.event.textChannel.sendMessage(parameterizedCommand.paramString).queue()
    }
}