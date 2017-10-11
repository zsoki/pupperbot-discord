package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase
import mu.KLogging

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase<ParameterizedCommand> {

    companion object : KLogging()

    override val onNext: (ParameterizedCommand) -> Unit = {
        it.event.message.delete().queue()
        it.event.textChannel.sendMessage(it.paramString).queue()
    }

    override val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        if (parameterizedCommand.paramString == null || parameterizedCommand.paramString.isEmpty()) {
            onError(IllegalArgumentException("Parameter string is null or empty"))
            return
        }

        onNext(parameterizedCommand)
    }
}